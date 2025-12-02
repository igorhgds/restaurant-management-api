package henrique.igor.restaurantmanagementapi.usecases.user;

import henrique.igor.restaurantmanagementapi.entities.User;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.request.CreateUserRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.response.UserResponseDTO;
import henrique.igor.restaurantmanagementapi.enums.UserRole;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.mapper.user.UserStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.user.UserJpaRepository;
import henrique.igor.restaurantmanagementapi.services.*;
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

    @Transactional
    public UserResponseDTO execute(CreateUserRequestDTO request) {
        var loggedUser = authService.getAutheticatedUser();

        this.validateRoleHierarchy(loggedUser.getUserRole(), request.userRole());

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

    private void validateRoleHierarchy(UserRole loggedUserRole, UserRole targetUserRole){
        if (loggedUserRole.equals(UserRole.ADMIN)) return;
        if (loggedUserRole.equals(UserRole.MANAGER) && !targetUserRole.equals(UserRole.ADMIN)) return;
        throw new BusinessRuleException(ExceptionCode.FORBIDDEN);
    }
}