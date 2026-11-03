package com.ecommerce.auth.infrastructure.driver_adapters.feign;

import com.ecommerce.auth.domain.model.gateway.NotificacionGateway;
import com.ecommerce.auth.infrastructure.driver_adapters.feign.dto.NotificacionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationGatewayImpl implements NotificacionGateway {

    private final NotificationFeignClient notificationFeignClient;

    @Override
    public void enviarNotificacion(String email, String telefono, String mensaje) {
        try {
            log.info("Enviando petición a NotificationFeignClient desde Auth para email: {}, telefono: {}", email, telefono);
            notificationFeignClient.enviarNotificacion(new NotificacionDTO(email, telefono, mensaje));
            log.info("Petición de notificación despachada exitosamente desde Auth.");
        } catch (Exception e) {
            log.error("Fallo al comunicarse con el microservicio de notificaciones desde Auth: {}", e.getMessage(), e);
        }
    }
}
