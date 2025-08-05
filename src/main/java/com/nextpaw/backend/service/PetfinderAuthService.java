package com.nextpaw.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

// Petfinder’s API requires an access token for every request
// Handles authentication with the Petfinder API
@Service
public class PetfinderAuthService {

    @Value("${petfinder.api.key}")
    private String apiKey;

    @Value("${petfinder.api.secret}")
    private String apiSecret;

    private String token;
    private long tokenExpiration;

    public String getAccessToken() {
        if (token == null || System.currentTimeMillis() >= tokenExpiration) {
            fetchAccessToken();
        }
        return token;
    }

    private void fetchAccessToken() {
        String url = "https://api.petfinder.com/v2/oauth2/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> form = new HashMap<>();
        form.put("grant_type", "client_credentials");
        form.put("client_id", apiKey);
        form.put("client_secret", apiSecret);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(form, headers);

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);

        token = (String) response.get("access_token");
        int expiresIn = (int) response.get("expires_in");
        tokenExpiration = System.currentTimeMillis() + (expiresIn * 1000);
    }
}
