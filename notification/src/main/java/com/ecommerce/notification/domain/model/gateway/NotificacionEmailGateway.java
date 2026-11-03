package com.ecommerce.notification.domain.model.gateway;

public interface NotificacionEmailGateway {

    void enviarEmail(String email, String mensaje);

}
