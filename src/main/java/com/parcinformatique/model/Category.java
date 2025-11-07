package com.parcinformatique.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    // Constructeur par défaut (OBLIGATOIRE pour Hibernate)
    public Category() {}

    // Constructeur avec paramètres
    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // GETTERS et SETTERS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "Category{id=" + id + ", name='" + name + "', description='" + description + "'}";
    }
}
