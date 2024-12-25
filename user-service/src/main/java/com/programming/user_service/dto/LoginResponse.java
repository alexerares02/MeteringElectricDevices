package com.programming.user_service.dto;

import java.util.UUID;

public record LoginResponse(UUID userId, String role, String token, long expiresIn) {
}
