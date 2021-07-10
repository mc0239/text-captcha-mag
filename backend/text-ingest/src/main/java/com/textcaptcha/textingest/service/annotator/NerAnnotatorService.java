package com.textcaptcha.textingest.service.annotator;

import com.textcaptcha.textingest.config.TextIngestConfigProvider;
import com.textcaptcha.textingest.dto.NerApiResponse;
import com.textcaptcha.textingest.exception.AnnotatorException;
import com.textcaptcha.textingest.pojo.annotator.NerAnnotatedToken;
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
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class NerAnnotatorService implements AnnotatorService<String, List<NerAnnotatedToken>> {

    private final TextIngestConfigProvider config;
    private final ExecutorService executor;
    private final RestTemplate rest;

    @Autowired
    public NerAnnotatorService(TextIngestConfigProvider config) {
        this.config = config;

        executor = Executors.newFixedThreadPool(10);
        rest = new RestTemplate();
        rest.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    @Override
    public List<NerAnnotatedToken> annotate(String text) throws AnnotatorException {
        Map<Integer, String> bodyChunks = splitTextToChunks(text);

        // Prepare futures for the executor (this allows concurrent POST requests to NER API to be made).
        Map<Integer, Future<List<NerAnnotatedToken>>> futures = new ConcurrentHashMap<>(bodyChunks.size());
        for (Map.Entry<Integer, String> entry : bodyChunks.entrySet()) {
            Callable<List<NerAnnotatedToken>> c = () -> annotateText(entry.getValue());
            futures.put(entry.getKey(), executor.submit(c));
        }

        // Store futures' results. Throws exception if any of the futures failed.
        Map<Integer, List<NerAnnotatedToken>> annotatedChunks = new ConcurrentHashMap<>(bodyChunks.size());
        for (Map.Entry<Integer, Future<List<NerAnnotatedToken>>> entry : futures.entrySet()) {
            Future<List<NerAnnotatedToken>> future = entry.getValue();
            try {
                annotatedChunks.put(entry.getKey(), future.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new AnnotatorException("NerAnnotatorService.annotate failed with an exception.", e);
            }
        }

        // Flatten Map<index, tokens chunk> to a list of tokens.
        List<NerAnnotatedToken> result = new ArrayList<>();
        for (int i = 0; i < annotatedChunks.size(); i++) {
            result.addAll(annotatedChunks.get(i));
        }
        return result;
    }

    private Map<Integer, String> splitTextToChunks(String text) {
        Map<Integer, String> chunks = new HashMap<>();

        StringBuilder currentChunk = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);

            currentChunk.append(currentChar);
            if (currentChunk.length() > 1_200) {
                if (currentChar == '.') {
                    chunks.put(chunks.size(), currentChunk.toString());
                    currentChunk = new StringBuilder();
                }
            }
        }

        if (currentChunk.length() > 1) {
            chunks.put(chunks.size(), currentChunk.toString());
        }

        return chunks;
    }

    private List<NerAnnotatedToken> annotateText(String text) throws RestClientException {
        String url = config.getNerUrl() + "/predict/ner";

        ResponseEntity<NerApiResponse> restResponse = rest.postForEntity(url, text, NerApiResponse.class);
        NerApiResponse response = restResponse.getBody();

        if (response != null) {
            return response.getTokens().stream().map(NerApiResponse.NerToken::toAnnotatedToken).collect(Collectors.toList());
        } else {
            throw new RestClientException("No response from annotator service (" + url + ").");
        }
    }

}
