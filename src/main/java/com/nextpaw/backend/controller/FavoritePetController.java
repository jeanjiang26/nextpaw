package com.nextpaw.backend.controller;

import com.nextpaw.backend.model.FavoritePet;
import com.nextpaw.backend.service.FavoritePetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "*")
public class FavoritePetController {

    @Autowired
    private FavoritePetService service;

    @PostMapping
    public FavoritePet addFavorite(@RequestBody FavoritePet pet) {
        return service.addFavorite(pet);
    }

    @GetMapping("/{userId}")
    public List<FavoritePet> getFavorites(@PathVariable Long userId) {
        return service.getFavorites(userId);
    }

    @DeleteMapping("/{userId}/{petId}")
    public void removeFavorite(@PathVariable Long userId, @PathVariable String petId) {
        service.removeFavorite(userId, petId);
    }
}
