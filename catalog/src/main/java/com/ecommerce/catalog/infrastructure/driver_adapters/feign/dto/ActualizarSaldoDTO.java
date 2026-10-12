package com.ecommerce.catalog.infrastructure.driver_adapters.feign.dto;

import java.math.BigDecimal;

public record ActualizarSaldoDTO(
        String idUsuario,
        BigDecimal saldo
) {}
