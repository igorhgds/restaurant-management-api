package henrique.igor.restaurantmanagementapi.usecases.auth;

import henrique.igor.restaurantmanagementapi.entities.User;
import henrique.igor.restaurantmanagementapi.entities.dtos.auth.request.ChangePasswordRequestDTO;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.repositories.user.UserJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangePasswordUseCase {

    private final UserJpaRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void execute(ChangePasswordRequestDTO request){
        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new EntityNotFoundException(UserJpaRepository.class));

        this.validateRecoveryCode(user, request);

        user.setPassword(bCryptPasswordEncoder.encode(request.password()));
        user.setPasswordRecoveryCode(null);
        userRepository.save(user);
    }

    private void validateRecoveryCode(User user, ChangePasswordRequestDTO request){
        if(!user.getPasswordRecoveryCode().equals(request.passwordRecoveryCode()))
            throw new BusinessRuleException(ExceptionCode.INVALID_ACTIVATION_CODE);
    }
}
