package com.ecommerce.auth.infrastructure.entry_points.dto;

public record LoginDTO(
        String email,
        String pass
) {
}
