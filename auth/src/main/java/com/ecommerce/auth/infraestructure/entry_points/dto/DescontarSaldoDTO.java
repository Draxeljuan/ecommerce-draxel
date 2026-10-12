package com.ecommerce.auth.infraestructure.entry_points.dto;

import java.math.BigDecimal;

public record DescontarSaldoDTO(
        String idUsuario,
        BigDecimal monto
) {
}
