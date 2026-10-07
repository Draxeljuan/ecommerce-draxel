package com.ecommerce.auth.infraestructure.entry_points.dto;

public record LoginDTO(
        String email,
        String pass
) {
}
