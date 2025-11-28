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
        var user = userRepository.findByEmail(request.email());

        if (user.isEmpty() || !this.isCodeValid(user.get(), request)){
            throw new BusinessRuleException(ExceptionCode.INVALID_CREDENTIALS);
        }

        var userValid = user.get();

        userValid.setPassword(bCryptPasswordEncoder.encode(request.password()));
        userValid.setPasswordRecoveryCode(null);
        userRepository.save(userValid);
    }

    private boolean isCodeValid(User user, ChangePasswordRequestDTO request){
        return user.getPasswordRecoveryCode() != null &&
                user.getPasswordRecoveryCode().equals(request.passwordRecoveryCode());
    }
}
