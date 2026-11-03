package com.ecommerce.notification.infrastructure.entry_points.dto;

public record NotificacionDTO(
        String email,
        String telefono,
        String mensaje
) {}
