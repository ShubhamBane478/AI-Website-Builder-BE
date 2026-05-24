package com.hustlerdev.aiwebsitebuilder.controller;

import com.hustlerdev.aiwebsitebuilder.dto.request.RegisterRequest;
import com.hustlerdev.aiwebsitebuilder.dto.response.AuthResponse;
import com.hustlerdev.aiwebsitebuilder.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request,   // @Valid triggers DTO validation
            HttpServletResponse response                   // needed to set refresh token cookie
    ) {
        AuthResponse authResponse = authService.register(request, response);
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }
}