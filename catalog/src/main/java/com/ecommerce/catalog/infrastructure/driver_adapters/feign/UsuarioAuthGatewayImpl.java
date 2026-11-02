package com.ecommerce.catalog.infrastructure.driver_adapters.feign;

import com.ecommerce.catalog.domain.exception.RecursoNoEncontradoException;
import com.ecommerce.catalog.domain.exception.ReglaNegocioException;
import com.ecommerce.catalog.domain.model.gateway.UsuarioAuthGateway;
import com.ecommerce.catalog.infrastructure.driver_adapters.feign.dto.DescontarSaldoDTO;
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
            return Boolean.TRUE.equals(usuarioAuthFeignClient.existeUsuario(idUsuario));
        } catch (FeignException.NotFound e) {
            return false;
        } catch (FeignException.BadRequest e) {
            throw new ReglaNegocioException(extraerMensaje(e, "Datos inválidos al verificar existencia de usuario"));
        } catch (FeignException e) {
            throw new ReglaNegocioException("Error al comunicarse con el servicio de autenticación: " + e.getMessage());
        }
    }

    @Override
    public BigDecimal obtenerSaldoUsuario(String idUsuario) {
        try {
            BigDecimal saldo = usuarioAuthFeignClient.obtenerSaldo(idUsuario);
            if (saldo == null) {
                throw new RecursoNoEncontradoException("No se encontró saldo para el usuario con ID: " + idUsuario);
            }
            return saldo;
        } catch (FeignException.NotFound e) {
            throw new RecursoNoEncontradoException("Usuario no encontrado con ID: " + idUsuario);
        } catch (FeignException.BadRequest e) {
            throw new ReglaNegocioException(extraerMensaje(e, "Datos inválidos al consultar saldo del usuario"));
        } catch (FeignException e) {
            throw new ReglaNegocioException("Error al comunicarse con el servicio de autenticación: " + e.getMessage());
        }
    }

    @Override
    public void descontarSaldo(String idUsuario, BigDecimal monto) {
        try {
            usuarioAuthFeignClient.descontarSaldo(new DescontarSaldoDTO(idUsuario, monto));
        } catch (FeignException.NotFound e) {
            throw new RecursoNoEncontradoException("Usuario no encontrado para descontar saldo: " + idUsuario);
        } catch (FeignException.BadRequest e) {
            throw new ReglaNegocioException(extraerMensaje(e, "No fue posible descontar el saldo del usuario"));
        } catch (FeignException e) {
            throw new ReglaNegocioException("Error al comunicarse con el servicio de autenticación: " + e.getMessage());
        }
    }

    private String extraerMensaje(FeignException e, String mensajePorDefecto) {
        String cuerpo = e.contentUTF8();
        return (cuerpo != null && !cuerpo.isBlank()) ? cuerpo : mensajePorDefecto;
    }
}
