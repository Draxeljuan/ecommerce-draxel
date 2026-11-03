package com.ecommerce.notification.domain.usecase;

import com.ecommerce.notification.domain.model.gateway.NotificacionEmailGateway;
import com.ecommerce.notification.domain.model.gateway.NotificacionSmsGateway;

public class NotificationUseCase {

    private final NotificacionEmailGateway notificacionEmailGateway;
    private final NotificacionSmsGateway notificacionSmsGateway;

    public NotificationUseCase(NotificacionEmailGateway notificacionEmailGateway,
                               NotificacionSmsGateway notificacionSmsGateway) {
        this.notificacionEmailGateway = notificacionEmailGateway;
        this.notificacionSmsGateway = notificacionSmsGateway;
    }

    public void enviarNotificacion(String email, String telefono, String mensaje) {
        if (mensaje == null || mensaje.isBlank()) {
            throw new IllegalArgumentException("El mensaje de notificación no puede estar vacío");
        }

        boolean tieneEmail = email != null && !email.isBlank();
        boolean tieneTelefono = telefono != null && !telefono.isBlank();

        if (!tieneEmail && !tieneTelefono) {
            throw new IllegalArgumentException("Debe proporcionar al menos un destino (email o teléfono)");
        }

        if (tieneEmail) {
            notificacionEmailGateway.enviarEmail(email.trim(), mensaje);
        }

        if (tieneTelefono) {
            notificacionSmsGateway.enviarSms(telefono.trim(), mensaje);
        }
    }

    public void enviarEmail(String email, String mensaje) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email de destino no puede estar vacío");
        }
        if (mensaje == null || mensaje.isBlank()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacío");
        }
        notificacionEmailGateway.enviarEmail(email.trim(), mensaje);
    }

    public void enviarSms(String telefono, String mensaje) {
        if (telefono == null || telefono.isBlank()) {
            throw new IllegalArgumentException("El teléfono de destino no puede estar vacío");
        }
        if (mensaje == null || mensaje.isBlank()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacío");
        }
        notificacionSmsGateway.enviarSms(telefono.trim(), mensaje);
    }
}
