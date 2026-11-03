package com.ecommerce.catalog.domain.model.gateway;

public interface NotificacionGateway {

    void enviarNotificacion(String email, String telefono, String mensaje);

}
