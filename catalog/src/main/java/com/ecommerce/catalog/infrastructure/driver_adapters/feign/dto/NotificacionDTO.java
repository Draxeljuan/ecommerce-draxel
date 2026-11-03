package com.ecommerce.catalog.infrastructure.driver_adapters.feign.dto;

public record NotificacionDTO(
        String email,
        String telefono,
        String mensaje
) {}
