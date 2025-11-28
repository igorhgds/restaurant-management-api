package henrique.igor.restaurantmanagementapi.usecases.auth;

import henrique.igor.restaurantmanagementapi.entities.User;
import henrique.igor.restaurantmanagementapi.entities.dtos.auth.request.GeneratePasswordRecoveryRequestDTO;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.repositories.user.UserJpaRepository;
import henrique.igor.restaurantmanagementapi.services.EmailService;
import henrique.igor.restaurantmanagementapi.services.RandomCodeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeneratePasswordRecoveryCodeUseCase {

    private final UserJpaRepository userRepository;
    private final RandomCodeService randomCodeService;
    private final EmailService emailService;

    @Transactional
    public void execute(GeneratePasswordRecoveryRequestDTO request){
        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new EntityNotFoundException(User.class));

        var temporaryCode = randomCodeService.generate(6);

        this.updateUser(user, temporaryCode);
        this.sendEmail(user, temporaryCode);
    }

    private void updateUser(User user, String temporaryCode){
        user.setPasswordRecoveryCode(temporaryCode);
        userRepository.save(user);
    }

    private void sendEmail(User user, String temporaryCode){
        emailService.sendRecoveryCode(user.getEmail(), temporaryCode);
    }
}
