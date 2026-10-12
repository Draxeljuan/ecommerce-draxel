package com.ecommerce.catalog.infrastructure.entry_points.dto;

import java.math.BigDecimal;
import java.util.List;

public record CheckoutResponseDTO(
        String idCarrito,
        String idUsuario,
        String estado,
        BigDecimal total,
        List<ItemCarritoDTO> items,
        String mensaje
) {}
