package com.ecommerce.catalog.infrastructure.driver_adapters.feign.dto;

import java.math.BigDecimal;

public record DescontarSaldoDTO(
        String idUsuario,
        BigDecimal monto
) {
}
