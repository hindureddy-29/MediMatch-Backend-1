package com.medimatch.service;

import com.medimatch.dto.AnalysisResponse;
import com.medimatch.dto.DuplicateMatchDto;
import com.medimatch.dto.MedicineInputItem;
import com.medimatch.entity.AnalysisMatchResult;
import com.medimatch.entity.AnalysisSession;
import com.medimatch.exception.ResourceNotFoundException;
import com.medimatch.repository.AnalysisSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HistoryService {

    private final AnalysisSessionRepository sessionRepository;

    @Autowired
    public HistoryService(AnalysisSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Transactional(readOnly = true)
    public List<AnalysisSession> getAllSessions() {
        return sessionRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<AnalysisSession> getUserSessions(Long userId) {
        if (userId == null) {
            return getAllSessions();
        }
        return sessionRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional(readOnly = true)
    public AnalysisResponse getSessionDetails(Long id) {
        AnalysisSession session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Analysis session not found with id: " + id));

        AnalysisResponse res = new AnalysisResponse();
        res.setSessionId(session.getId());
        res.setSessionTitle(session.getSessionTitle());
        res.setCreatedAt(session.getCreatedAt());
        res.setTotalInputMedicines(session.getTotalInputMedicines());
        res.setTotalDuplicatesFound(session.getTotalDuplicatesFound());
        res.setTotalUncertainMatches(session.getTotalUncertainMatches());

        List<DuplicateMatchDto> matchDtos = session.getResults()
                .stream()
                .map(DuplicateMatchDto::fromEntity)
                .collect(Collectors.toList());
        res.setMatches(matchDtos);

        // Reconstruct distinct input medicines from results
        List<MedicineInputItem> reconstructed = new ArrayList<>();
        session.getResults().forEach(r -> {
            if (reconstructed.stream().noneMatch(i -> i.getName().equalsIgnoreCase(r.getOriginalMedicineName()))) {
                reconstructed.add(new MedicineInputItem(r.getOriginalMedicineName(), r.getBrandName(), r.getActiveIngredient(), r.getStrength(), r.getDosageForm()));
            }
        });
        res.setAnalyzedItems(reconstructed);

        return res;
    }

    public void deleteSession(Long id) {
        if (!sessionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Analysis session not found with id: " + id);
        }
        sessionRepository.deleteById(id);
    }
}
