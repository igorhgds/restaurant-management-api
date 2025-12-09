package henrique.igor.restaurantmanagementapi.usecases.user;

import henrique.igor.restaurantmanagementapi.entities.User;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.request.CreateUserRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.response.UserResponseDTO;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.mapper.user.UserStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.user.UserJpaRepository;
import henrique.igor.restaurantmanagementapi.services.*;
import henrique.igor.restaurantmanagementapi.util.ValidateRoleHierarchy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserJpaRepository userRepository;
    private final RandomCodeService randomCodeService;
    private final UserStructMapper userMapper;
    private final EmailService emailService;
    private final AuthenticationContextService authService;
    private final ValidateRoleHierarchy validateRoleHierarchy;

    @Transactional
    public UserResponseDTO execute(CreateUserRequestDTO request) {
        var loggedUser = authService.getAutheticatedUser();

        validateRoleHierarchy.execute(loggedUser.getUserRole(), request.userRole());

        Map<String, String> errors = new HashMap<>();
        if(userRepository.findByUsername(request.username()).isPresent())
            errors.put("username", "user.username.duplicate");
        if(userRepository.findByEmail(request.email()).isPresent())
            errors.put("email", "user.email.duplicate");
        if (!errors.isEmpty())
            throw new BusinessRuleException(ExceptionCode.DUPLICATED_RESOURCE, errors);

        var temporaryCode = randomCodeService.generate(6);
        User user = userMapper.toEntity(request);
        user.setPasswordRecoveryCode(temporaryCode);

        emailService.sendActivationEmail(user.getEmail(), temporaryCode);

        return userMapper.toUserResponseDTO(userRepository.save(user));
    }
}