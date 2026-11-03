package com.ecommerce.auth.infrastructure.entry_points.dto;

public record UsuarioContactoDTO(
        String nombre,
        String email,
        String telefono
) {}
