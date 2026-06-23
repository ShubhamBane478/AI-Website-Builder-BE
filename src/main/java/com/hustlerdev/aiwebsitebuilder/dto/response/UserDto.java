package com.hustlerdev.aiwebsitebuilder.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@Schema(description = "Public profile of a registered user")
public class UserDto {

    @Schema(description = "Unique user ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Schema(description = "User's email address", example = "ajit@siteforge.dev")
    private String email;

    @Schema(description = "Number of sites the user has published", example = "0")
    private Integer sitesPublished;

    @Schema(description = "Maximum sites allowed on the user's plan", example = "3")
    private Integer sitesLimit;
}