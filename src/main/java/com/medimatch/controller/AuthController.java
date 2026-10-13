package com.medimatch.controller;

import com.medimatch.dto.ApiResponse;
import com.medimatch.dto.AuthRequest;
import com.medimatch.dto.AuthResponse;
import com.medimatch.dto.RegisterRequest;
import com.medimatch.entity.User;
import com.medimatch.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest req) {
        AuthResponse res = authService.register(req);
        return ResponseEntity.ok(ApiResponse.ok("Registration successful", res));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest req) {
        AuthResponse res = authService.login(req);
        return ResponseEntity.ok(ApiResponse.ok("Login successful", res));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Object>> getCurrentUser(@AuthenticationPrincipal Object principal) {
        if (principal instanceof User) {
            User user = (User) principal;
            return ResponseEntity.ok(ApiResponse.ok(new AuthResponse(null, user.getId(), user.getUsername(), user.getEmail(), user.getRole())));
        }
        return ResponseEntity.ok(ApiResponse.ok("Guest user", null));
    }
}
