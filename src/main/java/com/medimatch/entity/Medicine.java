package com.medimatch.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "medicines", indexes = {
    @Index(name = "idx_med_brand", columnList = "brand_name"),
    @Index(name = "idx_med_generic", columnList = "generic_name"),
    @Index(name = "idx_med_ingredient", columnList = "active_ingredient")
})
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand_name", nullable = false, length = 150)
    private String brandName;

    @Column(name = "generic_name", nullable = false, length = 150)
    private String genericName;

    @Column(name = "active_ingredient", nullable = false, length = 200)
    private String activeIngredient;

    @Column(length = 60)
    private String strength;

    @Column(name = "dosage_form", length = 60)
    private String dosageForm;

    @Column(length = 150)
    private String manufacturer;

    @Column(length = 100)
    private String category;

    @Column(nullable = false)
    private Boolean verified = true;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Medicine() {
    }

    public Medicine(String brandName, String genericName, String activeIngredient,
                    String strength, String dosageForm, String manufacturer,
                    String category) {
        this(brandName, genericName, activeIngredient, strength, dosageForm, manufacturer, category, true, "");
    }

    public Medicine(String brandName, String genericName, String activeIngredient,
                    String strength, String dosageForm, String manufacturer,
                    String category, Boolean verified, String description) {
        this.brandName = brandName;
        this.genericName = genericName;
        this.activeIngredient = activeIngredient;
        this.strength = strength;
        this.dosageForm = dosageForm;
        this.manufacturer = manufacturer;
        this.category = category;
        this.verified = verified != null ? verified : true;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getActiveIngredient() {
        return activeIngredient;
    }

    public void setActiveIngredient(String activeIngredient) {
        this.activeIngredient = activeIngredient;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
