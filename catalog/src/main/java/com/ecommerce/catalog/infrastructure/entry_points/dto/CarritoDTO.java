package com.ecommerce.catalog.infrastructure.entry_points.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CarritoDTO(
        String idCarrito,
        String idUsuario,
        String estado,
        LocalDateTime fechaCreacion,
        List<ItemCarritoDTO> items
) {}
