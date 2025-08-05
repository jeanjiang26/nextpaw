package com.nextpaw.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class OpenAiService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private final File RECOMMENDATIONS_FILE = new File("src/main/resources/recommendations.json");

    public String getPetRecommendation(String userMessage) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);

        Map<String, Object> request = new HashMap<>();
        request.put("model", "gpt-4o");

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", "You are a pet expert helping users choose the best pet."));
        messages.add(Map.of("role", "user", "content", userMessage));
        request.put("messages", messages);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(OPENAI_API_URL, entity, Map.class);

        Map<String, Object> choices = ((List<Map<String, Object>>) response.getBody().get("choices")).get(0);
        Map<String, String> message = (Map<String, String>) choices.get("message");
        String reply = message.get("content");

        // Append reply to JSON file
        try {
            Map<String, List<String>> data;

            // If file exists and not empty, load it
            if (RECOMMENDATIONS_FILE.exists() && RECOMMENDATIONS_FILE.length() > 0) {
                data = mapper.readValue(RECOMMENDATIONS_FILE, new TypeReference<>() {});
            } else {
                data = new HashMap<>();
                data.put("responses", new ArrayList<>());
            }

            data.get("responses").add(reply);
            mapper.writerWithDefaultPrettyPrinter().writeValue(RECOMMENDATIONS_FILE, data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return reply;
    }
}
