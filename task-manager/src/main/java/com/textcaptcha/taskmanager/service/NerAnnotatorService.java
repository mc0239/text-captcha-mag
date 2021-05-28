package com.textcaptcha.taskmanager.service;

import com.textcaptcha.taskmanager.config.TaskManagerConfigProvider;
import com.textcaptcha.taskmanager.model.AnnotatedToken;
import com.textcaptcha.taskmanager.model.ner.NerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class NerAnnotatorService implements AnnotatorService {

    private final TaskManagerConfigProvider config;
    private final ExecutorService service;
    private final RestTemplate rest;

    @Autowired
    public NerAnnotatorService(TaskManagerConfigProvider config) {
        this.config = config;

        service = Executors.newFixedThreadPool(10);
        rest = new RestTemplate();
        rest.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    @Override
    public List<AnnotatedToken> annotate(String text) {
        Map<Integer, String> bodyChunks = splitTextToChunks(text);
        Map<Integer, List<AnnotatedToken>> annotatedChunks = new ConcurrentHashMap<>(bodyChunks.size());

        // Prepare tasks for the executor (this allows concurrent POST requests to NER API to be made).
        List<Callable<Object>> tasks = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : bodyChunks.entrySet()) {
            tasks.add(Executors.callable(() -> {
                annotatedChunks.put(entry.getKey(), annotateText(entry.getValue()));
            }));
        }

        try {
            service.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Flatten Map<index, tokens chunk> to a list of tokens.
        List<AnnotatedToken> result = new ArrayList<>();
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

    private List<AnnotatedToken> annotateText(String text) {
        ResponseEntity<NerResponse> restResponse = rest.postForEntity(config.getNerUrl() + "/predict/ner", text, NerResponse.class);
        NerResponse response = restResponse.getBody();
        if (response != null) {
            return response.getTokens().stream().map(AnnotatedToken::fromNerToken).collect(Collectors.toList());
        } else {
            // TODO this is probably an error.
            return Collections.emptyList();
        }
    }

}
