package com.parcinformatique.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "equipments")
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 50)
    private String brand;

    @Column(length = 50)
    private String model;

    @Column(unique = true, length = 100)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentStatus status = EquipmentStatus.AVAILABLE;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Relation ManyToOne avec Category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    //Relation OneToMany avec Assignment
    @OneToMany(mappedBy = "equipment")
    private List<Assignment> assignments;


    public Equipment() {}

    public Equipment(String name, String brand, String model, String serialNumber) {
        this.name = name;
        this.brand = brand;
        this.model = model;
        this.serialNumber = serialNumber;
    }

    public Equipment(String name, String brand, String model, String serialNumber,
                     EquipmentStatus status, LocalDate purchaseDate) {
        this.name = name;
        this.brand = brand;
        this.model = model;
        this.serialNumber = serialNumber;
        this.status = status;
        this.purchaseDate = purchaseDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

    public EquipmentStatus getStatus() { return status; }
    public void setStatus(EquipmentStatus status) { this.status = status; }

    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public String getFullName() {
        return brand + " " + model + " - " + name;
    }

    @Override
    public String toString() {
        return "Equipment{id=" + id + ", name='" + name + "', serial='" + serialNumber + "', status=" + status + "}";
    }
}