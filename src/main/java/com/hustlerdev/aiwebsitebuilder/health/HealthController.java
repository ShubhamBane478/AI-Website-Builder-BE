package com.hustlerdev.aiwebsitebuilder.health;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api")
public class    HealthController {

    private static final String SERVICE_NAME = "aiwebsitebuilder";

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        return ResponseEntity.ok(new HealthResponse(
                "UP",
                SERVICE_NAME,
                resolveEnvironment(),
                Instant.now()
        ));
    }

    private String resolveEnvironment() {
        if (System.getenv("VERCEL") != null) return "vercel";
        if (System.getenv("RAILWAY_ENVIRONMENT") != null || System.getenv("RAILWAY_SERVICE_NAME") != null) return "railway";
        if (System.getenv("DOCKER_ENV") != null) return "docker";
        return "local";
    }
}
