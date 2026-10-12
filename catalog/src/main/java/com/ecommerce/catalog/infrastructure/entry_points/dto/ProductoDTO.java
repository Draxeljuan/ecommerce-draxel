package com.ecommerce.catalog.infrastructure.entry_points.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductoDTO(
        String idProducto,
        String nombre,
        String descripcion,
        BigDecimal precio,
        Integer stock,
        LocalDateTime fechaCreacion
) {}
