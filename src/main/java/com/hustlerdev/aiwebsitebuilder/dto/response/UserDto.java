package com.hustlerdev.aiwebsitebuilder.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserDto {
    private UUID id;
    private String email;
    private Integer sitesPublished;
    private Integer sitesLimit;
}