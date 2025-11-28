package henrique.igor.restaurantmanagementapi.services;

public interface EmailService {
    void sendActivationEmail(String to, String tempPassword);

    void sendRecoveryCode(String to, String tempPassword);
}
