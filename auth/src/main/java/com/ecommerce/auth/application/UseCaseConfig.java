package com.ecommerce.auth.application;

import com.ecommerce.auth.domain.model.gateway.UsuarioGateway;
import com.ecommerce.auth.domain.usecase.UsuarioUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {
    @Bean // Spring Administra el Caso de Uso aqui con Bean
    public UsuarioUseCase usuarioUseCase(UsuarioGateway usuarioGateway) {
        // Spring automáticamente buscará la implementación del gateway (@Repository) y la pasará aquí
        return new UsuarioUseCase(usuarioGateway);
    }
}
