package com.ecommerce.auth.infraestructure.entry_points.dto;

public record UsuarioDTO(
        String idUsuario,
        String nombre,
        String email,
        String pass,
        String role,
        Integer edad,
        String numeroTelefono
) {}

