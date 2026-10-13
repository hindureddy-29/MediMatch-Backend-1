package com.medimatch.repository;

import com.medimatch.entity.AnalysisSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalysisSessionRepository extends JpaRepository<AnalysisSession, Long> {

    List<AnalysisSession> findAllByOrderByCreatedAtDesc();

    List<AnalysisSession> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT COALESCE(SUM(s.totalInputMedicines), 0) FROM AnalysisSession s")
    Long sumTotalInputMedicines();

    @Query("SELECT COALESCE(SUM(s.totalDuplicatesFound), 0) FROM AnalysisSession s")
    Long sumTotalDuplicatesFound();

    @Query("SELECT COALESCE(SUM(s.totalUncertainMatches), 0) FROM AnalysisSession s")
    Long sumTotalUncertainMatches();
}
