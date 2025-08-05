package com.nextpaw.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class RecommendationService {

    public List<Map<String, Object>> getHypoallergenicKidFriendly() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = getClass().getClassLoader().getResourceAsStream("recommendations.json");
            Map<String, List<Map<String, Object>>> data = mapper.readValue(is, Map.class);
            return data.get("hypoallergenic_good_with_kids");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load recommendations", e);
        }
    }
}
