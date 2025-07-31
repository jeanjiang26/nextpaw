package com.nextpaw.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "app_user") // <-- fixes the issue
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String lifestyle;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLifestyle() { return lifestyle; }
    public void setLifestyle(String lifestyle) { this.lifestyle = lifestyle; }
}
