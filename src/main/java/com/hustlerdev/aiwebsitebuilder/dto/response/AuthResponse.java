package com.hustlerdev.aiwebsitebuilder.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Returned after a successful register or login")
public class AuthResponse {

    @Schema(description = "Short-lived JWT access token — include as 'Authorization: Bearer <token>' on protected requests",
            example = "eyJhbGciOiJIUzI1NiJ9...")
    private String accessToken;

    @Schema(description = "Basic profile of the authenticated user")
    private UserDto user;
}