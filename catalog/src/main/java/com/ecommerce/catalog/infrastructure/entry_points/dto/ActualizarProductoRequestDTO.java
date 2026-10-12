package com.ecommerce.catalog.infrastructure.entry_points.dto;

import java.math.BigDecimal;

public record ActualizarProductoRequestDTO(
        String idProducto,
        String nombre,
        String descripcion,
        BigDecimal precio,
        Integer stock
) {}

