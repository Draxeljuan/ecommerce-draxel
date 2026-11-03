package com.ecommerce.notification.application;

import com.ecommerce.notification.domain.model.gateway.NotificacionEmailGateway;
import com.ecommerce.notification.domain.model.gateway.NotificacionSmsGateway;
import com.ecommerce.notification.domain.usecase.NotificationUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public NotificationUseCase notificationUseCase(NotificacionEmailGateway notificacionEmailGateway,
                                                   NotificacionSmsGateway notificacionSmsGateway) {
        return new NotificationUseCase(notificacionEmailGateway, notificacionSmsGateway);
    }
}
