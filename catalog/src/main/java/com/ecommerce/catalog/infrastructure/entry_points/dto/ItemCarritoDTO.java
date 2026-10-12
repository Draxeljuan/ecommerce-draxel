package com.ecommerce.catalog.infrastructure.entry_points.dto;

import java.math.BigDecimal;

public record ItemCarritoDTO(
        String idItem,
        String idCarrito,
        String idProducto,
        Integer cantidad,
        BigDecimal precioUnitario
) {}
