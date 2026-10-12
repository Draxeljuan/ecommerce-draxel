package com.ecommerce.catalog.infrastructure.driver_adapters.feign;

import com.ecommerce.catalog.domain.exception.RecursoNoEncontradoException;
import com.ecommerce.catalog.domain.exception.ReglaNegocioException;
import com.ecommerce.catalog.domain.model.gateway.UsuarioAuthGateway;
import com.ecommerce.catalog.infrastructure.driver_adapters.feign.dto.ActualizarSaldoDTO;
import com.ecommerce.catalog.infrastructure.driver_adapters.feign.dto.UsuarioResponseDTO;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class UsuarioAuthGatewayImpl implements UsuarioAuthGateway {

    private final UsuarioAuthFeignClient usuarioAuthFeignClient;

    @Override
    public boolean existeUsuario(String idUsuario) {
        try {
            UsuarioResponseDTO usuario = usuarioAuthFeignClient.buscarUsuarioPorId(idUsuario);
            return usuario != null;
        } catch (FeignException.NotFound e) {
            return false;
        } catch (FeignException e) {
            throw new ReglaNegocioException("Error al comunicarse con el servicio de autenticación: " + e.getMessage());
        }
    }

    @Override
    public BigDecimal obtenerSaldoUsuario(String idUsuario) {
        try {
            UsuarioResponseDTO usuario = usuarioAuthFeignClient.buscarUsuarioPorId(idUsuario);
            if (usuario == null || usuario.saldo() == null) {
                throw new RecursoNoEncontradoException("Usuario no encontrado en el servicio de autenticación");
            }
            return usuario.saldo();
        } catch (FeignException.NotFound e) {
            throw new RecursoNoEncontradoException("Usuario no encontrado con ID: " + idUsuario);
        } catch (FeignException e) {
            throw new ReglaNegocioException("Error al comunicarse con el servicio de autenticación: " + e.getMessage());
        }
    }

    @Override
    public void actualizarSaldo(String idUsuario, BigDecimal nuevoSaldo) {
        try {
            usuarioAuthFeignClient.actualizarSaldo(new ActualizarSaldoDTO(idUsuario, nuevoSaldo));
        } catch (FeignException.NotFound e) {
            throw new RecursoNoEncontradoException("Usuario no encontrado para actualizar saldo: " + idUsuario);
        } catch (FeignException e) {
            throw new ReglaNegocioException("Error al actualizar saldo en el servicio de autenticación: " + e.getMessage());
        }
    }
}
