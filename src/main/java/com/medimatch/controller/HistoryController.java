package com.medimatch.controller;

import com.medimatch.dto.AnalysisResponse;
import com.medimatch.dto.ApiResponse;
import com.medimatch.entity.AnalysisSession;
import com.medimatch.entity.User;
import com.medimatch.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    private final HistoryService historyService;

    @Autowired
    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AnalysisSession>>> getHistory(@AuthenticationPrincipal Object principal) {
        Long userId = null;
        if (principal instanceof User) {
            userId = ((User) principal).getId();
        }
        List<AnalysisSession> sessions = historyService.getUserSessions(userId);
        return ResponseEntity.ok(ApiResponse.ok(sessions));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AnalysisResponse>> getSessionDetails(@PathVariable Long id) {
        AnalysisResponse details = historyService.getSessionDetails(id);
        return ResponseEntity.ok(ApiResponse.ok(details));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSession(@PathVariable Long id) {
        historyService.deleteSession(id);
        return ResponseEntity.ok(ApiResponse.ok("Analysis session deleted successfully", null));
    }
}
