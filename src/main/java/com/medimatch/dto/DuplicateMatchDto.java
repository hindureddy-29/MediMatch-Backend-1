package com.medimatch.dto;

import com.medimatch.entity.AnalysisMatchResult;

public class DuplicateMatchDto {
    private Long id;
    private String originalMedicineName;
    private String standardizedMedicineName;
    private String matchedMedicineName;
    private String standardizedMatchedName;
    private String brandName;
    private String activeIngredient;
    private String strength;
    private String dosageForm;
    private Double similarityScore;
    private String matchClassification;
    private String reason;
    private String verificationStatus;
    private String clinicalNotice;
    private String matchSource;

    public DuplicateMatchDto() {}

    public static DuplicateMatchDto fromEntity(AnalysisMatchResult r) {
        DuplicateMatchDto dto = new DuplicateMatchDto();
        dto.setId(r.getId());
        dto.setOriginalMedicineName(r.getOriginalMedicineName());
        dto.setMatchedMedicineName(r.getMatchedMedicineName());
        dto.setBrandName(r.getBrandName());
        dto.setActiveIngredient(r.getActiveIngredient());
        dto.setStrength(r.getStrength());
        dto.setDosageForm(r.getDosageForm());
        dto.setSimilarityScore(r.getSimilarityScore());
        dto.setMatchClassification(r.getMatchClassification());
        dto.setReason(r.getReason());
        dto.setVerificationStatus(r.getVerificationStatus());
        dto.setClinicalNotice(r.getClinicalNotice());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalMedicineName() {
        return originalMedicineName;
    }

    public void setOriginalMedicineName(String originalMedicineName) {
        this.originalMedicineName = originalMedicineName;
    }

    public String getStandardizedMedicineName() {
        return standardizedMedicineName;
    }

    public void setStandardizedMedicineName(String standardizedMedicineName) {
        this.standardizedMedicineName = standardizedMedicineName;
    }

    public String getMatchedMedicineName() {
        return matchedMedicineName;
    }

    public void setMatchedMedicineName(String matchedMedicineName) {
        this.matchedMedicineName = matchedMedicineName;
    }

    public String getStandardizedMatchedName() {
        return standardizedMatchedName;
    }

    public void setStandardizedMatchedName(String standardizedMatchedName) {
        this.standardizedMatchedName = standardizedMatchedName;
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

    public Double getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(Double similarityScore) {
        this.similarityScore = similarityScore;
    }

    public String getMatchClassification() {
        return matchClassification;
    }

    public void setMatchClassification(String matchClassification) {
        this.matchClassification = matchClassification;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public String getClinicalNotice() {
        return clinicalNotice;
    }

    public void setClinicalNotice(String clinicalNotice) {
        this.clinicalNotice = clinicalNotice;
    }

    public String getMatchSource() {
        return matchSource;
    }

    public void setMatchSource(String matchSource) {
        this.matchSource = matchSource;
    }
}
