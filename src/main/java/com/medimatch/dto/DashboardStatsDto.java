package com.medimatch.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardStatsDto {
    private Long totalMedicinesCatalog = 0L;
    private Long verifiedMedicinesCatalog = 0L;
    private Long totalMedicinesAnalyzed = 0L;
    private Long totalDuplicatePairsFound = 0L;
    private Long totalUncertainMatches = 0L;
    private Long totalSessions = 0L;
    private Map<String, Long> classificationBreakdown = new HashMap<>();
    private List<RecentSessionItem> recentSessions = new ArrayList<>();
    private List<FrequentMedicineItem> frequentlyMatched = new ArrayList<>();

    public DashboardStatsDto() {}

    public static class RecentSessionItem {
        private Long id;
        private String sessionTitle;
        private Integer totalInputMedicines;
        private Integer totalDuplicatesFound;
        private Integer totalUncertainMatches;
        private String createdAt;

        public RecentSessionItem() {}

        public RecentSessionItem(Long id, String sessionTitle, Integer totalInputMedicines,
                                 Integer totalDuplicatesFound, Integer totalUncertainMatches, String createdAt) {
            this.id = id;
            this.sessionTitle = sessionTitle;
            this.totalInputMedicines = totalInputMedicines;
            this.totalDuplicatesFound = totalDuplicatesFound;
            this.totalUncertainMatches = totalUncertainMatches;
            this.createdAt = createdAt;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getSessionTitle() { return sessionTitle; }
        public void setSessionTitle(String sessionTitle) { this.sessionTitle = sessionTitle; }
        public Integer getTotalInputMedicines() { return totalInputMedicines; }
        public void setTotalInputMedicines(Integer totalInputMedicines) { this.totalInputMedicines = totalInputMedicines; }
        public Integer getTotalDuplicatesFound() { return totalDuplicatesFound; }
        public void setTotalDuplicatesFound(Integer totalDuplicatesFound) { this.totalDuplicatesFound = totalDuplicatesFound; }
        public Integer getTotalUncertainMatches() { return totalUncertainMatches; }
        public void setTotalUncertainMatches(Integer totalUncertainMatches) { this.totalUncertainMatches = totalUncertainMatches; }
        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    }

    public static class FrequentMedicineItem {
        private String medicineName;
        private Long count;

        public FrequentMedicineItem() {}

        public FrequentMedicineItem(String medicineName, Long count) {
            this.medicineName = medicineName;
            this.count = count;
        }

        public String getMedicineName() { return medicineName; }
        public void setMedicineName(String medicineName) { this.medicineName = medicineName; }
        public Long getCount() { return count; }
        public void setCount(Long count) { this.count = count; }
    }

    public Long getTotalMedicinesCatalog() { return totalMedicinesCatalog; }
    public void setTotalMedicinesCatalog(Long totalMedicinesCatalog) { this.totalMedicinesCatalog = totalMedicinesCatalog; }
    public Long getVerifiedMedicinesCatalog() { return verifiedMedicinesCatalog; }
    public void setVerifiedMedicinesCatalog(Long verifiedMedicinesCatalog) { this.verifiedMedicinesCatalog = verifiedMedicinesCatalog; }
    public Long getTotalMedicinesAnalyzed() { return totalMedicinesAnalyzed; }
    public void setTotalMedicinesAnalyzed(Long totalMedicinesAnalyzed) { this.totalMedicinesAnalyzed = totalMedicinesAnalyzed; }
    public Long getTotalDuplicatePairsFound() { return totalDuplicatePairsFound; }
    public void setTotalDuplicatePairsFound(Long totalDuplicatePairsFound) { this.totalDuplicatePairsFound = totalDuplicatePairsFound; }
    public Long getTotalUncertainMatches() { return totalUncertainMatches; }
    public void setTotalUncertainMatches(Long totalUncertainMatches) { this.totalUncertainMatches = totalUncertainMatches; }
    public Long getTotalSessions() { return totalSessions; }
    public void setTotalSessions(Long totalSessions) { this.totalSessions = totalSessions; }
    public Map<String, Long> getClassificationBreakdown() { return classificationBreakdown; }
    public void setClassificationBreakdown(Map<String, Long> classificationBreakdown) { this.classificationBreakdown = classificationBreakdown; }
    public List<RecentSessionItem> getRecentSessions() { return recentSessions; }
    public void setRecentSessions(List<RecentSessionItem> recentSessions) { this.recentSessions = recentSessions; }
    public List<FrequentMedicineItem> getFrequentlyMatched() { return frequentlyMatched; }
    public void setFrequentlyMatched(List<FrequentMedicineItem> frequentlyMatched) { this.frequentlyMatched = frequentlyMatched; }
}
