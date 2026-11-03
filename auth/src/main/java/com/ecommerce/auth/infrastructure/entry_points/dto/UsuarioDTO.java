package com.ecommerce.auth.infrastructure.entry_points.dto;

import java.math.BigDecimal;

public record UsuarioDTO(
        String idUsuario,
        String nombre,
        String email,
        String pass,
        String role,
        Integer edad,
        String numeroTelefono,
        BigDecimal saldo
) {}
