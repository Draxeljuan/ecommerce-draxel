package com.ecommerce.catalog.infrastructure.driver_adapters.feign;

import com.ecommerce.catalog.infrastructure.driver_adapters.feign.dto.DescontarSaldoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

@FeignClient(name = "auth-service", url = "${auth.service.url:http://localhost:9090}")
public interface UsuarioAuthFeignClient {

    @GetMapping("/api/ecommerce/usuario/{id}/existe")
    Boolean existeUsuario(@PathVariable("id") String id);

    @GetMapping("/api/ecommerce/usuario/{id}/saldo")
    BigDecimal obtenerSaldo(@PathVariable("id") String id);

    @PutMapping("/api/ecommerce/usuario/descontar-saldo")
    void descontarSaldo(@RequestBody DescontarSaldoDTO dto);
}
