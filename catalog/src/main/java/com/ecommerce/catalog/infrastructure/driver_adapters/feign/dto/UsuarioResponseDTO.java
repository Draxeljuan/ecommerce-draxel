package com.ecommerce.catalog.infrastructure.driver_adapters.feign.dto;

import java.math.BigDecimal;

public record UsuarioResponseDTO(
        String idUsuario,
        String nombre,
        String email,
        String role,
        Integer edad,
        String numeroTelefono,
        BigDecimal saldo
) {}
