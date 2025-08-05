package com.nextpaw.backend.repository;

import com.nextpaw.backend.model.FavoritePet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritePetRepository extends JpaRepository<FavoritePet, Long> {
    // Change String userId to Long userId and use property path
    List<FavoritePet> findByUser_Id(Long userId);

    // Change String userId to Long userId
    void deleteByUser_IdAndPetId(Long userId, String petId);
}