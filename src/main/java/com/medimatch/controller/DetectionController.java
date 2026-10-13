package com.medimatch.controller;

import com.medimatch.dto.AnalysisRequest;
import com.medimatch.dto.AnalysisResponse;
import com.medimatch.dto.ApiResponse;
import com.medimatch.entity.User;
import com.medimatch.service.DuplicateDetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/detection")
public class DetectionController {

    private final DuplicateDetectionService detectionService;

    @Autowired
    public DetectionController(DuplicateDetectionService detectionService) {
        this.detectionService = detectionService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<ApiResponse<AnalysisResponse>> analyze(
            @RequestBody AnalysisRequest request,
            @AuthenticationPrincipal Object principal) {

        Long userId = null;
        String username = "Guest User";

        if (principal instanceof User) {
            User user = (User) principal;
            userId = user.getId();
            username = user.getUsername();
        }

        AnalysisResponse response = detectionService.analyzeMedicines(request, userId, username);
        return ResponseEntity.ok(ApiResponse.ok("Analysis completed successfully", response));
    }
}
