package com.medimatch.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "analysis_match_results", indexes = {
    @Index(name = "idx_match_orig", columnList = "original_medicine_name"),
    @Index(name = "idx_match_matched", columnList = "matched_medicine_name"),
    @Index(name = "idx_match_class", columnList = "match_classification")
})
public class AnalysisMatchResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    @JsonBackReference
    private AnalysisSession session;

    @Column(name = "original_medicine_name", nullable = false, length = 150)
    private String originalMedicineName;

    @Column(name = "matched_medicine_name", nullable = false, length = 150)
    private String matchedMedicineName;

    @Column(name = "brand_name", length = 150)
    private String brandName;

    @Column(name = "active_ingredient", length = 200)
    private String activeIngredient;

    @Column(length = 60)
    private String strength;

    @Column(name = "dosage_form", length = 60)
    private String dosageForm;

    @Column(name = "similarity_score", nullable = false)
    private Double similarityScore;

    @Column(name = "match_classification", nullable = false, length = 60)
    private String matchClassification;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "verification_status", length = 50)
    private String verificationStatus = "VERIFIED";

    @Column(name = "clinical_notice", columnDefinition = "TEXT")
    private String clinicalNotice;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public AnalysisMatchResult() {
    }

    public AnalysisMatchResult(AnalysisSession session, String originalMedicineName,
                               String matchedMedicineName, String brandName,
                               String activeIngredient, String strength,
                               String dosageForm, Double similarityScore,
                               String matchClassification, String reason,
                               String verificationStatus, String clinicalNotice) {
        this.session = session;
        this.originalMedicineName = originalMedicineName;
        this.matchedMedicineName = matchedMedicineName;
        this.brandName = brandName;
        this.activeIngredient = activeIngredient;
        this.strength = strength;
        this.dosageForm = dosageForm;
        this.similarityScore = similarityScore;
        this.matchClassification = matchClassification;
        this.reason = reason;
        this.verificationStatus = verificationStatus != null ? verificationStatus : "VERIFIED";
        this.clinicalNotice = clinicalNotice;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AnalysisSession getSession() {
        return session;
    }

    public void setSession(AnalysisSession session) {
        this.session = session;
    }

    public String getOriginalMedicineName() {
        return originalMedicineName;
    }

    public void setOriginalMedicineName(String originalMedicineName) {
        this.originalMedicineName = originalMedicineName;
    }

    public String getMatchedMedicineName() {
        return matchedMedicineName;
    }

    public void setMatchedMedicineName(String matchedMedicineName) {
        this.matchedMedicineName = matchedMedicineName;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
