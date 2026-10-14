package com.ecommerce.auth.infraestructure.entry_points.dto;

import java.math.BigDecimal;

public record SaldoActualizadoDTO(
        String nombre,
        BigDecimal saldo
) {
}