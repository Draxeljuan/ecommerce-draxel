package com.ecommerce.catalog.infrastructure.driver_adapters.feign;

import com.ecommerce.catalog.infrastructure.driver_adapters.feign.dto.ActualizarSaldoDTO;
import com.ecommerce.catalog.infrastructure.driver_adapters.feign.dto.UsuarioResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
// Estudiar definición y explicación de cómo funciona esta anotación
@FeignClient(name = "auth-service", url = "${auth.service.url:http://localhost:9090}")
public interface UsuarioAuthFeignClient {

    @GetMapping("/api/ecommerce/usuario/{id}")
    UsuarioResponseDTO buscarUsuarioPorId(@PathVariable("id") String id);

    @PutMapping("/api/ecommerce/usuario/saldo")
    UsuarioResponseDTO actualizarSaldo(@RequestBody ActualizarSaldoDTO saldoDTO);
}
