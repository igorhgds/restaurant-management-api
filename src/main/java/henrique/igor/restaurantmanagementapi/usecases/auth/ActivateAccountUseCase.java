package henrique.igor.restaurantmanagementapi.usecases.auth;

import henrique.igor.restaurantmanagementapi.entities.User;
import henrique.igor.restaurantmanagementapi.entities.dtos.auth.request.ActivateAccountRequestDTO;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.repositories.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActivateAccountUseCase {

    private final UserJpaRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void execute(ActivateAccountRequestDTO request){
        Optional<User> user = userRepository.findByEmail(request.email());

        if (user.isEmpty() || !this.isCodeValid(user.get(), request)){
            throw new BusinessRuleException(ExceptionCode.INVALID_CREDENTIALS);
        }

        var userValid = user.get();
        userValid.setPassword(passwordEncoder.encode(request.password()));
        userValid.setEnabled(true);
        userValid.setPasswordRecoveryCode(null);
        userRepository.save(userValid);
    }

    private boolean isCodeValid(User user, ActivateAccountRequestDTO request) {
        return user.getPasswordRecoveryCode() != null &&
                user.getPasswordRecoveryCode().equals(request.passwordRecoveryCode());
    }
}
