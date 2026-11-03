package com.ecommerce.notification.domain.model.gateway;

public interface NotificacionSmsGateway {

    void enviarSms(String telefono, String mensaje);

}
