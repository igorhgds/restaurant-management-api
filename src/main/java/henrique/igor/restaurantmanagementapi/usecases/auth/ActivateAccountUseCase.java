package henrique.igor.restaurantmanagementapi.usecases.auth;

import henrique.igor.restaurantmanagementapi.entities.User;
import henrique.igor.restaurantmanagementapi.entities.dtos.auth.request.ActivateAccountRequestDTO;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.repositories.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivateAccountUseCase {

    private final UserJpaRepository userRepository;
    //TODO BCrypt

    public void activateAccount(ActivateAccountRequestDTO request){
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessRuleException(ExceptionCode.ENTITY_NOT_FOUND));

        if (!request.passwordRecoveryCode().equals(user.getPasswordRecoveryCode())){
            throw new BusinessRuleException(ExceptionCode.INVALID_ACTIVATION_CODE);
        }

        user.setPasswordRecoveryCode(null);
        user.setPassword(request.password());
        user.setEnabled(true);

        userRepository.save(user);
    }
}
