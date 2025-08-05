package com.nextpaw.backend.controller;

import com.nextpaw.backend.service.PetfinderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

// api controller that receives http requests from frontend or postman and delegates to service layer
@RestController
@RequestMapping("/api/pets")
@CrossOrigin(origins = "*")
public class PetfinderController {

    @Autowired
    private PetfinderService petfinderService;

    // ✅ Search endpoint with full filters
    @GetMapping
    public Map<String, Object> getPets(
            @RequestParam(defaultValue = "Los Angeles, CA") String location,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String age,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String size,
            @RequestParam(required = false, name = "good_with_children") Boolean goodWithChildren,
            @RequestParam(required = false, name = "good_with_dogs") Boolean goodWithDogs,
            @RequestParam(required = false, name = "good_with_cats") Boolean goodWithCats
    ) {
        return petfinderService.searchPets(location, type, breed, age, gender, size, goodWithChildren, goodWithDogs, goodWithCats);
    }

    // ✅ Get dog breeds
    @GetMapping("/breeds/dog")
    public Map<String, Object> getDogBreeds() {
        return petfinderService.getDogBreeds();
    }

    // ✅ Get cat breeds
    @GetMapping("/breeds/cat")
    public Map<String, Object> getCatBreeds() {
        return petfinderService.getCatBreeds();
    }


    @GetMapping("/{id}")
    public Map<String, Object> getPetById(@PathVariable String id) {
        return petfinderService.getPetById(id);
    }

}
