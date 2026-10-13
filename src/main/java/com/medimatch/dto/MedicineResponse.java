package com.medimatch.dto;

import com.medimatch.entity.Medicine;
import java.time.LocalDateTime;

public class MedicineResponse {
    private Long id;
    private String brandName;
    private String genericName;
    private String activeIngredient;
    private String strength;
    private String dosageForm;
    private String manufacturer;
    private String category;
    private Boolean verified;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MedicineResponse() {}

    public static MedicineResponse fromEntity(Medicine m) {
        MedicineResponse res = new MedicineResponse();
        res.setId(m.getId());
        res.setBrandName(m.getBrandName());
        res.setGenericName(m.getGenericName());
        res.setActiveIngredient(m.getActiveIngredient());
        res.setStrength(m.getStrength());
        res.setDosageForm(m.getDosageForm());
        res.setManufacturer(m.getManufacturer());
        res.setCategory(m.getCategory());
        res.setVerified(m.getVerified());
        res.setDescription(m.getDescription());
        res.setCreatedAt(m.getCreatedAt());
        res.setUpdatedAt(m.getUpdatedAt());
        return res;
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
