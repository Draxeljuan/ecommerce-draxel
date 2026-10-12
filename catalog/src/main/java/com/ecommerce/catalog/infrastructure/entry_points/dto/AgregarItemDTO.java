package com.ecommerce.catalog.infrastructure.entry_points.dto;

public record AgregarItemDTO(
        String idUsuario,
        String idProducto,
        Integer cantidad
) {}
