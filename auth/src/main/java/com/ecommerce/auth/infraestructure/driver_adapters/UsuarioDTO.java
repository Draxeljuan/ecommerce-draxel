package com.ecommerce.auth.infraestructure.driver_adapters;

public record UsuarioDTO(
        String idUsuario,
        String nombre,
        String email,
        String pass,
        String role,
        Integer edad,
        String numeroTelefono
) {}

