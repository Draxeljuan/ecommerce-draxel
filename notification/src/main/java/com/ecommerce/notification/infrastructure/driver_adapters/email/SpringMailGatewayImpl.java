package com.ecommerce.notification.infrastructure.driver_adapters.email;

import com.ecommerce.notification.domain.model.gateway.NotificacionEmailGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpringMailGatewayImpl implements NotificacionEmailGateway {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    @Async
    @Override
    public void enviarEmail(String email, String mensaje) {
        try {
            log.info("Enviando correo electrónico asíncrono a: {}", email);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            if (fromEmail != null && !fromEmail.isBlank()) {
                mailMessage.setFrom(fromEmail);
            }
            mailMessage.setTo(email);
            mailMessage.setSubject("Notificación E-commerce");
            mailMessage.setText(mensaje);

            javaMailSender.send(mailMessage);
            log.info("Correo electrónico enviado exitosamente a: {}", email);
        } catch (Exception e) {
            log.error("Fallo al enviar correo electrónico a {}: {}", email, e.getMessage(), e);
        }
    }
}
