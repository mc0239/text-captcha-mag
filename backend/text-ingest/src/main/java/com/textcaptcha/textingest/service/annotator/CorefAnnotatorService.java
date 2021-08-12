package com.textcaptcha.textingest.service.annotator;

import com.textcaptcha.textingest.config.TextIngestConfigProvider;
import com.textcaptcha.textingest.dto.ClasslaApiResponse;
import com.textcaptcha.textingest.dto.CorefApiRequestBody;
import com.textcaptcha.textingest.dto.CorefApiResponse;
import com.textcaptcha.textingest.exception.AnnotatorException;
import com.textcaptcha.textingest.pojo.annotator.CorefAnnotatedToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CorefAnnotatorService implements AnnotatorService<ClasslaApiResponse, List<CorefAnnotatedToken>> {

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
    public List<CorefAnnotatedToken> annotate(ClasslaApiResponse input) throws AnnotatorException {
        String url = config.getCorefUrl() + "/predict/coref";

        CorefApiRequestBody requestBody = classlaResponseToCorefRequest(input);

        ResponseEntity<CorefApiResponse> restResponse = rest.postForEntity(url, requestBody, CorefApiResponse.class);
        CorefApiResponse response = restResponse.getBody();

        if (response == null) {
            throw new RestClientException("No response from annotator service (" + url + ").");
        }

        List<CorefApiRequestBody.Token> tokensWithMentions = requestBody.getSentences()
                .stream()
                // flatten tokens in sentences into a single tokens list.
                .flatMap(sentence -> sentence.getTokens().stream())
                .collect(Collectors.toList());

        List<CorefAnnotatedToken> tokensWithClusters = new ArrayList<>();
        int i = 0;
        for (CorefApiRequestBody.Token t : tokensWithMentions) {
            CorefAnnotatedToken ct = new CorefAnnotatedToken();
            ct.setIndex(i);
            ct.setWord(t.getText());
            ct.setMentionId(t.getMentionId());
            ct.setClusterId(response.getClusters().getOrDefault(t.getMentionId(), null));
            tokensWithClusters.add(ct);
            i++;
        }

        return tokensWithClusters;
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
                // TODO what about nested mentions?
                if (inputToken.getXpos().startsWith("N")/* || inputToken.getXpos().startsWith("A")*/) {
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
