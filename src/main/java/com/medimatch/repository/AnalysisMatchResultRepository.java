package com.medimatch.repository;

import com.medimatch.entity.AnalysisMatchResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalysisMatchResultRepository extends JpaRepository<AnalysisMatchResult, Long> {

    List<AnalysisMatchResult> findBySessionId(Long sessionId);

    long countByMatchClassification(String matchClassification);

    @Query("SELECT r.matchedMedicineName, COUNT(r) as cnt " +
           "FROM AnalysisMatchResult r " +
           "GROUP BY r.matchedMedicineName " +
           "ORDER BY cnt DESC")
    List<Object[]> findFrequentlyMatchedMedicines(Pageable pageable);
}
