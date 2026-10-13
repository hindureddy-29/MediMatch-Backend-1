package com.medimatch.dto;

import java.util.ArrayList;
import java.util.List;

public class AnalysisRequest {
    private String rawText;
    private List<MedicineInputItem> items = new ArrayList<>();
    private String sessionTitle;
    private Boolean saveToHistory = true;
    private Boolean compareWithDatabase = true;

    public AnalysisRequest() {}

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public List<MedicineInputItem> getItems() {
        return items;
    }

    public void setItems(List<MedicineInputItem> items) {
        this.items = items;
    }

    public String getSessionTitle() {
        return sessionTitle;
    }

    public void setSessionTitle(String sessionTitle) {
        this.sessionTitle = sessionTitle;
    }

    public Boolean getSaveToHistory() {
        return saveToHistory;
    }

    public void setSaveToHistory(Boolean saveToHistory) {
        this.saveToHistory = saveToHistory;
    }

    public Boolean getCompareWithDatabase() {
        return compareWithDatabase;
    }

    public void setCompareWithDatabase(Boolean compareWithDatabase) {
        this.compareWithDatabase = compareWithDatabase;
    }
}
