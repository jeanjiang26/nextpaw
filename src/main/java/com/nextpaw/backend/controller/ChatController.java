package com.nextpaw.backend.controller;

import com.nextpaw.backend.service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private OpenAiService openAiService;

    @GetMapping
    public ResponseEntity<String> chat(@RequestParam String message) {
        String reply = openAiService.getPetRecommendation(message);
        return ResponseEntity.ok(reply);
    }
}
