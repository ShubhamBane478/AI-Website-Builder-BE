package com.hustlerdev.aiwebsitebuilder.health;

import java.time.Instant;

public record HealthResponse(
        String status,
        String service,
        String environment,
        Instant timestamp
) {}