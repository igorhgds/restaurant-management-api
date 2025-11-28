package henrique.igor.restaurantmanagementapi.usecases.auth;

import henrique.igor.restaurantmanagementapi.entities.User;
import henrique.igor.restaurantmanagementapi.entities.dtos.auth.request.GeneratePasswordRecoveryRequestDTO;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.repositories.user.UserJpaRepository;
import henrique.igor.restaurantmanagementapi.services.EmailService;
import henrique.igor.restaurantmanagementapi.services.RandomCodeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeneratePasswordRecoveryCodeUseCase {

    private final UserJpaRepository userRepository;
    private final RandomCodeService randomCodeService;
    private final EmailService emailService;

    @Transactional
    public void execute(GeneratePasswordRecoveryRequestDTO request){
        Optional<User> user = userRepository.findByEmail(request.email());

        if (user.isEmpty()) {
            log.warn("Attempt to recover password for a non-existent email address.: {}", request.email());
            return;
        }

        var userValid = user.get();
        var temporaryCode = randomCodeService.generate(6);

        this.updateUser(userValid, temporaryCode);
        this.sendEmail(userValid, temporaryCode);
    }

    private void updateUser(User user, String temporaryCode){
        user.setPasswordRecoveryCode(temporaryCode);
        userRepository.save(user);
    }

    private void sendEmail(User user, String temporaryCode){
        emailService.sendRecoveryCode(user.getEmail(), temporaryCode);
    }
}
