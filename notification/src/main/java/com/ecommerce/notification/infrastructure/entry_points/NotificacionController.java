package com.ecommerce.notification.infrastructure.entry_points;

import com.ecommerce.notification.domain.usecase.NotificationUseCase;
import com.ecommerce.notification.infrastructure.entry_points.dto.NotificacionDTO;
import com.ecommerce.notification.infrastructure.entry_points.dto.NotificacionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ecommerce/notificacion")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificationUseCase notificationUseCase;

    @PostMapping("/enviar")
    public ResponseEntity<NotificacionResponseDTO> enviarNotificacion(@RequestBody NotificacionDTO dto) {
        notificationUseCase.enviarNotificacion(dto.email(), dto.telefono(), dto.mensaje());
        return ResponseEntity.ok(
                new NotificacionResponseDTO("PROCESANDO", "Notificación recibida y puesta en cola para despacho")
        );
    }
}
