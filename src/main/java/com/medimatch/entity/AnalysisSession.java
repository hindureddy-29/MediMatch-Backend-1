package com.medimatch.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "analysis_sessions")
public class AnalysisSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_title", length = 150)
    private String sessionTitle;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", length = 60)
    private String username;

    @Column(name = "total_input_medicines")
    private Integer totalInputMedicines = 0;

    @Column(name = "total_duplicates_found")
    private Integer totalDuplicatesFound = 0;

    @Column(name = "total_uncertain_matches")
    private Integer totalUncertainMatches = 0;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<AnalysisMatchResult> results = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public AnalysisSession() {
    }

    public AnalysisSession(String sessionTitle, Long userId, String username,
                           Integer totalInputMedicines, Integer totalDuplicatesFound,
                           Integer totalUncertainMatches) {
        this.sessionTitle = sessionTitle;
        this.userId = userId;
        this.username = username;
        this.totalInputMedicines = totalInputMedicines;
        this.totalDuplicatesFound = totalDuplicatesFound;
        this.totalUncertainMatches = totalUncertainMatches;
        this.createdAt = LocalDateTime.now();
    }

    public void addResult(AnalysisMatchResult result) {
        results.add(result);
        result.setSession(this);
    }

    public void removeResult(AnalysisMatchResult result) {
        results.remove(result);
        result.setSession(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionTitle() {
        return sessionTitle;
    }

    public void setSessionTitle(String sessionTitle) {
        this.sessionTitle = sessionTitle;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public List<AnalysisMatchResult> getResults() {
        return results;
    }

    public void setResults(List<AnalysisMatchResult> results) {
        this.results = results;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
