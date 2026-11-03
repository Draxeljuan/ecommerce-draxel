package com.ecommerce.notification.infrastructure.driver_adapters.sms;

import com.ecommerce.notification.domain.model.gateway.NotificacionSmsGateway;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TwilioSmsGatewayImpl implements NotificacionSmsGateway {

    @Value("${twilio.account.sid:}")
    private String accountSid;

    @Value("${twilio.auth.token:}")
    private String authToken;

    @Value("${twilio.phone.number:}")
    private String fromNumber;

    @PostConstruct
    public void init() {
        if (accountSid != null && !accountSid.isBlank() && authToken != null && !authToken.isBlank()) {
            Twilio.init(accountSid, authToken);
            log.info("Twilio SDK inicializado exitosamente.");
        } else {
            log.warn("Credenciales de Twilio no configuradas en application.properties.");
        }
    }

    @Async
    @Override
    public void enviarSms(String telefono, String mensaje) {
        if (telefono == null || telefono.isBlank()) {
            return;
        }

        String telefonoNormalizado = normalizarTelefono(telefono);
        try {
            String template = seleccionarPlantilla(mensaje);
            log.info("Despachando SMS a destinatario: {} con plantilla: {}", telefonoNormalizado, template);

            Message message = Message.creator(
                    new PhoneNumber(telefonoNormalizado),
                    new PhoneNumber(fromNumber),
                    template
            ).create();

            log.info("SMS enviado exitosamente. SID: {}", message.getSid());
        } catch (ApiException e) {
            // Ignorar excepción si el número no está registrado como receptor verificado en Twilio
            if (e.getCode() == 572002 || (e.getMessage() != null && e.getMessage().contains("verified recipient"))) {
                log.info("SMS omitido: el número '{}' no está registrado como receptor verificado en Twilio.", telefonoNormalizado);
            } else {
                log.error("Error al enviar SMS vía Twilio a {}: {}", telefonoNormalizado, e.getMessage());
            }
        } catch (Exception e) {
            log.error("Error al procesar el envío de SMS a {}: {}", telefonoNormalizado, e.getMessage());
        }
    }

    private String seleccionarPlantilla(String mensaje) {
        if (mensaje != null) {
            String msgLower = mensaje.toLowerCase();
            if (msgLower.contains("compra") || msgLower.contains("carrito") || msgLower.contains("checkout") || msgLower.contains("orden")) {
                return "sms_order_confirmation";
            }
            if (msgLower.contains("login") || msgLower.contains("sesión") || msgLower.contains("sesion") || msgLower.contains("cuenta") || msgLower.contains("autenticado")) {
                return "sms_account_alerts";
            }
        }
        return "sms_account_alerts";
    }

    private String normalizarTelefono(String telefono) {
        if (telefono == null) {
            return null;
        }
        String limpio = telefono.replaceAll("[\\s\\-\\(\\)]", "").trim();
        if (limpio.startsWith("+")) {
            return limpio;
        }
        if (limpio.length() == 10 && limpio.matches("\\d{10}")) {
            return "+57" + limpio;
        }
        if (limpio.startsWith("57") && limpio.length() == 12) {
            return "+" + limpio;
        }
        return "+" + limpio;
    }
}
