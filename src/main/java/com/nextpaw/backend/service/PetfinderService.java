package com.nextpaw.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;


// logic layer that handles communication with petfinder api
@Service
public class PetfinderService {

    @Value("${petfinder.api.key}")
    private String apiKey;

    @Value("${petfinder.api.secret}")
    private String apiSecret;

    private String accessToken;
    private long tokenExpiration = 0;

    public Map<String, Object> searchPets(
            String location, // city, zip code
            String type, // dog, cat
            String breed,
            String age,
            String gender,
            String size, // small, medium, large, xlarge
            Boolean goodWithChildren,
            Boolean goodWithDogs,
            Boolean goodWithCats,


            Boolean houseTrained,
            Boolean spayedNeutered,
            Boolean specialNeeds,
            Boolean declawed,
            Boolean hasShots,
            Boolean shotsCurrent
    ) {
        ensureAccessToken();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        StringBuilder urlBuilder = new StringBuilder("https://api.petfinder.com/v2/animals?limit=50");

        if (location != null && !location.isEmpty()) {
            urlBuilder.append("&location=").append(URLEncoder.encode(location, StandardCharsets.UTF_8));
        }
        if (type != null && !type.isEmpty()) {
            urlBuilder.append("&type=").append(type);
        }
        if (breed != null && !breed.isEmpty()) {
            urlBuilder.append("&breed=").append(URLEncoder.encode(breed, StandardCharsets.UTF_8));
        }
        if (age != null && !age.isEmpty()) {
            urlBuilder.append("&age=").append(age);
        }
        if (gender != null && !gender.isEmpty()) {
            urlBuilder.append("&gender=").append(gender);
        }
        if (size != null && !size.isEmpty()) {
            urlBuilder.append("&size=").append(size);
        }
        if (goodWithChildren != null) {
            urlBuilder.append("&good_with_children=").append(goodWithChildren);
        }
        if (goodWithDogs != null) {
            urlBuilder.append("&good_with_dogs=").append(goodWithDogs);
        }
        if (goodWithCats != null) {
            urlBuilder.append("&good_with_cats=").append(goodWithCats);
        }

        if (houseTrained != null) {
            urlBuilder.append("&house_trained=").append(houseTrained);
        }
        if (spayedNeutered != null) {
            urlBuilder.append("&spayed_neutered=").append(spayedNeutered);
        }
        if (specialNeeds != null) {
            urlBuilder.append("&special_needs=").append(specialNeeds);
        }
        if (declawed != null) {
            urlBuilder.append("&declawed=").append(declawed);
        }
        if (hasShots != null) {
            urlBuilder.append("&has_shots=").append(hasShots);
        }
        if (shotsCurrent != null) {
            urlBuilder.append("&shots_current=").append(shotsCurrent);
        }

        ResponseEntity<Map> response = restTemplate.exchange(
                urlBuilder.toString(),
                HttpMethod.GET,
                entity,
                Map.class
        );

        return response.getBody();
    }

    public Map<String, Object> getDogBreeds() {
        ensureAccessToken();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = "https://api.petfinder.com/v2/types/dog/breeds";

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        return response.getBody();
    }

    public Map<String, Object> getCatBreeds() {
        ensureAccessToken();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = "https://api.petfinder.com/v2/types/cat/breeds";

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        return response.getBody();
    }

    private void ensureAccessToken() {
        if (accessToken == null || System.currentTimeMillis() >= tokenExpiration) {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add("grant_type", "client_credentials");
            form.add("client_id", apiKey);
            form.add("client_secret", apiSecret);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.petfinder.com/v2/oauth2/token",
                    request,
                    Map.class
            );

            Map<String, Object> tokenData = response.getBody();
            accessToken = (String) tokenData.get("access_token");
            Integer expiresIn = (Integer) tokenData.get("expires_in");
            tokenExpiration = System.currentTimeMillis() + (expiresIn * 1000L) - 5000;
        }
    }

    public Map<String, Object> getPetById(String id) {
        ensureAccessToken();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://api.petfinder.com/v2/animals/" + id;

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class
        );

        return response.getBody();
    }

}
