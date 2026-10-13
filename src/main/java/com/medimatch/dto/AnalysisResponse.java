package com.medimatch.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AnalysisResponse {
    private Long sessionId;
    private String sessionTitle;
    private LocalDateTime createdAt = LocalDateTime.now();
    private Integer totalInputMedicines = 0;
    private Integer totalDuplicatesFound = 0;
    private Integer totalUncertainMatches = 0;
    private List<DuplicateMatchDto> matches = new ArrayList<>();
    private List<MedicineInputItem> analyzedItems = new ArrayList<>();
    private String clinicalDisclaimer = "MediMatch AI is a decision-support and duplicate medicine screening tool. It does not replace medical advice, diagnosis, or clinical prescription review by a qualified healthcare professional. Never change or discontinue medications without consulting a licensed physician or pharmacist.";
    private Long executionTimeMs = 0L;

    public AnalysisResponse() {}

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionTitle() {
        return sessionTitle;
    }

    public void setSessionTitle(String sessionTitle) {
        this.sessionTitle = sessionTitle;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getTotalInputMedicines() {
        return totalInputMedicines;
    }

    public void setTotalInputMedicines(Integer totalInputMedicines) {
        this.totalInputMedicines = totalInputMedicines;
    }

    public Integer getTotalDuplicatesFound() {
        return totalDuplicatesFound;
    }

    public void setTotalDuplicatesFound(Integer totalDuplicatesFound) {
        this.totalDuplicatesFound = totalDuplicatesFound;
    }

    public Integer getTotalUncertainMatches() {
        return totalUncertainMatches;
    }

    public void setTotalUncertainMatches(Integer totalUncertainMatches) {
        this.totalUncertainMatches = totalUncertainMatches;
    }

    public List<DuplicateMatchDto> getMatches() {
        return matches;
    }

    public void setMatches(List<DuplicateMatchDto> matches) {
        this.matches = matches;
    }

    public List<MedicineInputItem> getAnalyzedItems() {
        return analyzedItems;
    }

    public void setAnalyzedItems(List<MedicineInputItem> analyzedItems) {
        this.analyzedItems = analyzedItems;
    }

    public String getClinicalDisclaimer() {
        return clinicalDisclaimer;
    }

    public void setClinicalDisclaimer(String clinicalDisclaimer) {
        this.clinicalDisclaimer = clinicalDisclaimer;
    }

    public Long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void setExecutionTimeMs(Long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }
}
