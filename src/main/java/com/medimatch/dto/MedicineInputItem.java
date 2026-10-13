package com.medimatch.dto;

public class MedicineInputItem {
    private String name;
    private String brandName;
    private String activeIngredient;
    private String strength;
    private String dosageForm;

    public MedicineInputItem() {}

    public MedicineInputItem(String name) {
        this.name = name;
    }

    public MedicineInputItem(String name, String brandName, String activeIngredient, String strength, String dosageForm) {
        this.name = name;
        this.brandName = brandName;
        this.activeIngredient = activeIngredient;
        this.strength = strength;
        this.dosageForm = dosageForm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
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
}
