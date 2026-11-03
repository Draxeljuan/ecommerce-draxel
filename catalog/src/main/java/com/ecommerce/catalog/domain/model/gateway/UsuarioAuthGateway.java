package com.ecommerce.catalog.domain.model.gateway;

import com.ecommerce.catalog.domain.model.UsuarioContacto;

import java.math.BigDecimal;

public interface UsuarioAuthGateway {
    boolean existeUsuario(String idUsuario);
    BigDecimal obtenerSaldoUsuario(String idUsuario);
    void descontarSaldo(String idUsuario, BigDecimal monto);
    UsuarioContacto obtenerContactoUsuario(String idUsuario);
}
