package com.ecommerce.catalog.infrastructure.driver_adapters.feign;

import com.ecommerce.catalog.infrastructure.driver_adapters.feign.dto.NotificacionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "${notification.service.url:http://localhost:9092}")
public interface NotificationFeignClient {

    @PostMapping("/api/ecommerce/notificacion/enviar")
    void enviarNotificacion(@RequestBody NotificacionDTO dto);

}
