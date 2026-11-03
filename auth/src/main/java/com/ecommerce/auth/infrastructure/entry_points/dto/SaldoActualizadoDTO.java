package com.ecommerce.auth.infrastructure.entry_points.dto;

import java.math.BigDecimal;

public record SaldoActualizadoDTO(
        String nombre,
        BigDecimal saldo
) {
}