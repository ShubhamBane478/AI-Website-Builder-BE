package com.hustlerdev.aiwebsitebuilder.health;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@Tag(name = "Health", description = "Service health check")
@RestController
@RequestMapping("/api")
public class HealthController {

    private static final String SERVICE_NAME = "aiwebsitebuilder";

    @Operation(summary = "Health check", description = "Returns service status, name, and current environment")
    @ApiResponse(responseCode = "200", description = "Service is up",
        content = @Content(schema = @Schema(implementation = HealthResponse.class)))
    @SecurityRequirements
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
