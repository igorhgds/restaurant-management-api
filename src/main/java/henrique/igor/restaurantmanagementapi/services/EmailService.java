package henrique.igor.restaurantmanagementapi.services;

import org.springframework.context.annotation.Bean;

public interface EmailService {
    void sendActivationEmail(String to, String tempPassword);
}
