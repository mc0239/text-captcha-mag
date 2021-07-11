package com.textcaptcha.textingest.service.annotator;

import com.textcaptcha.textingest.config.TextIngestConfigProvider;
import com.textcaptcha.textingest.dto.ClasslaApiResponse;
import com.textcaptcha.textingest.dto.CorefApiRequestBody;
import com.textcaptcha.textingest.dto.CorefApiResponse;
import com.textcaptcha.textingest.exception.AnnotatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CorefAnnotatorService implements AnnotatorService<ClasslaApiResponse, CorefApiResponse> {

    private final TextIngestConfigProvider config;
    private final RestTemplate rest;

    @Autowired
    public CorefAnnotatorService(TextIngestConfigProvider config) {
        this.config = config;

        rest = new RestTemplate();
        rest.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    @Override
    public CorefApiResponse annotate(ClasslaApiResponse input) throws AnnotatorException {
        String url = config.getCorefUrl() + "/predict/coref";

        CorefApiRequestBody requestBody = classlaResponseToCorefRequest(input);

        ResponseEntity<CorefApiResponse> restResponse = rest.postForEntity(url, requestBody, CorefApiResponse.class);
        CorefApiResponse response = restResponse.getBody();

        if (response != null) {
            // cluster1 -> [mention1, mention2 ...]
            Map<Integer, List<Integer>> clusters = new HashMap<>();
            for (Map.Entry<Integer, Integer> entry : response.getClusters().entrySet()) {
                Integer clusterId = entry.getValue();
                Integer mentionId = entry.getKey();
                if (!clusters.containsKey(clusterId)) {
                    clusters.put(clusterId, new ArrayList<>());
                }
                clusters.get(clusterId).add(mentionId);
            }
            return response;
        } else {
            throw new RestClientException("No response from annotator service (" + url + ").");
        }
    }

    private CorefApiRequestBody classlaResponseToCorefRequest(ClasslaApiResponse input) {
        CorefApiRequestBody output = new CorefApiRequestBody();
        int mentionId = 1;
        boolean consecutiveToken = false;

        for (ClasslaApiResponse.Sentence inputSentence : input.getSentences()) {
            CorefApiRequestBody.Sentence outputSentence = new CorefApiRequestBody.Sentence();

            for (ClasslaApiResponse.Token inputToken : inputSentence.getTokens()) {
                CorefApiRequestBody.Token outputToken = new CorefApiRequestBody.Token();
                outputToken.setText(inputToken.getText());

                // TODO what constitutes as a "mention"?
                if (inputToken.getXpos().startsWith("N") || inputToken.getXpos().startsWith("A")) {
                    outputToken.setMentionId(mentionId);
                    if (!consecutiveToken) {
                        consecutiveToken = true;
                    }
                } else {
                    if (consecutiveToken) {
                        mentionId++;
                    }
                    consecutiveToken = false;
                }

                outputSentence.getTokens().add(outputToken);
            }

            output.getSentences().add(outputSentence);
        }

        return output;
    }

}
