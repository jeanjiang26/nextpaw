package com.nextpaw.backend.service;

import com.nextpaw.backend.model.FavoritePet;
import com.nextpaw.backend.repository.FavoritePetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoritePetService {

    @Autowired
    private FavoritePetRepository repository;

    public FavoritePet addFavorite(FavoritePet favoritePet) {
        return repository.save(favoritePet);
    }

    // Fix: Change String to Long and update method name
    public List<FavoritePet> getFavorites(Long userId) {
        System.out.println("Type of userId: " + userId.getClass().getSimpleName());
        return repository.findByUser_Id(userId);
    }


    // Fix: Change String userId to Long and update method name
    public void removeFavorite(Long userId, String petId) {
        repository.deleteByUser_IdAndPetId(userId, petId);  // Updated method name
    }
}