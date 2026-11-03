package com.ecommerce.catalog.infrastructure.driver_adapters.feign.dto;

public record UsuarioContactoDTO(
        String nombre,
        String email,
        String telefono
) {}
