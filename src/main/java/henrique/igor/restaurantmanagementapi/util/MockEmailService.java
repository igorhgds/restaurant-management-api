package henrique.igor.restaurantmanagementapi.util;

import henrique.igor.restaurantmanagementapi.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MockEmailService implements EmailService {

    @Override
    public void sendActivationEmail(String destinationEmail, String activationCode) {
        log.info("--------------------------------------------------");
        log.info("[EMAIL MOCK] Enviando email para: {}", destinationEmail);
        log.info("[EMAIL MOCK] Assunto: Ative sua conta");
        log.info("[EMAIL MOCK] Corpo: Olá! Seu código de ativação é: {}", activationCode);
        log.info("--------------------------------------------------");
    }

    @Override
    public void sendRecoveryCode(String destinationEmail, String activationCode) {
        log.info("--------------------------------------------------");
        log.info("[EMAIL MOCK] Enviando email para: {}", destinationEmail);
        log.info("[EMAIL MOCK] Assunto: Código para alterar senha");
        log.info("[EMAIL MOCK] Corpo: Olá! Seu código de ativação é: {}", activationCode);
        log.info("--------------------------------------------------");
    }
}