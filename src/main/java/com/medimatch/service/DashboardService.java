package com.medimatch.service;

import com.medimatch.dto.DashboardStatsDto;
import com.medimatch.entity.AnalysisSession;
import com.medimatch.repository.AnalysisMatchResultRepository;
import com.medimatch.repository.AnalysisSessionRepository;
import com.medimatch.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final MedicineRepository medicineRepository;
    private final AnalysisSessionRepository sessionRepository;
    private final AnalysisMatchResultRepository matchResultRepository;

    @Autowired
    public DashboardService(MedicineRepository medicineRepository,
                            AnalysisSessionRepository sessionRepository,
                            AnalysisMatchResultRepository matchResultRepository) {
        this.medicineRepository = medicineRepository;
        this.sessionRepository = sessionRepository;
        this.matchResultRepository = matchResultRepository;
    }

    public DashboardStatsDto getDashboardStats() {
        DashboardStatsDto stats = new DashboardStatsDto();

        stats.setTotalMedicinesCatalog(medicineRepository.count());
        stats.setVerifiedMedicinesCatalog(medicineRepository.countByVerifiedTrue());
        stats.setTotalMedicinesAnalyzed(sessionRepository.sumTotalInputMedicines());
        stats.setTotalDuplicatePairsFound(sessionRepository.sumTotalDuplicatesFound());
        stats.setTotalUncertainMatches(sessionRepository.sumTotalUncertainMatches());
        stats.setTotalSessions(sessionRepository.count());

        // Classification breakdown
        Map<String, Long> breakdown = new HashMap<>();
        breakdown.put("Exact name match", matchResultRepository.countByMatchClassification("Exact name match"));
        breakdown.put("Possible ingredient duplicate", matchResultRepository.countByMatchClassification("Possible ingredient duplicate"));
        breakdown.put("Possible name match", matchResultRepository.countByMatchClassification("Possible name match"));
        breakdown.put("Uncertain match", matchResultRepository.countByMatchClassification("Uncertain match"));
        breakdown.put("Insufficient medicine information", matchResultRepository.countByMatchClassification("Insufficient medicine information"));
        stats.setClassificationBreakdown(breakdown);

        // Recent 5 sessions
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<AnalysisSession> recent = sessionRepository.findAllByOrderByCreatedAtDesc();
        List<DashboardStatsDto.RecentSessionItem> recentItems = recent.stream()
                .limit(5)
                .map(s -> new DashboardStatsDto.RecentSessionItem(
                        s.getId(),
                        s.getSessionTitle(),
                        s.getTotalInputMedicines(),
                        s.getTotalDuplicatesFound(),
                        s.getTotalUncertainMatches(),
                        s.getCreatedAt() != null ? s.getCreatedAt().format(formatter) : ""
                ))
                .collect(Collectors.toList());
        stats.setRecentSessions(recentItems);

        // Top frequently matched medicines
        List<Object[]> freqRows = matchResultRepository.findFrequentlyMatchedMedicines(PageRequest.of(0, 5));
        List<DashboardStatsDto.FrequentMedicineItem> freqItems = freqRows.stream()
                .map(r -> new DashboardStatsDto.FrequentMedicineItem((String) r[0], (Long) r[1]))
                .collect(Collectors.toList());
        stats.setFrequentlyMatched(freqItems);

        return stats;
    }
}
