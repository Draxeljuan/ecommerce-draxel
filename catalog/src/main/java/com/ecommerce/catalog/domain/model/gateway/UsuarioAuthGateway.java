package com.ecommerce.catalog.domain.model.gateway;

import java.math.BigDecimal;

public interface UsuarioAuthGateway {
    boolean existeUsuario(String idUsuario);
    BigDecimal obtenerSaldoUsuario(String idUsuario);
    void actualizarSaldo(String idUsuario, BigDecimal nuevoSaldo);
}
