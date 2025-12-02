package henrique.igor.restaurantmanagementapi.usecases.user;

import henrique.igor.restaurantmanagementapi.entities.User;
import henrique.igor.restaurantmanagementapi.enums.UserRole;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.repositories.user.UserJpaRepository;
import henrique.igor.restaurantmanagementapi.services.AuthenticationContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteUserByIdUseCase {

    private final UserJpaRepository userRepository;
    private final AuthenticationContextService authService;

    public void execute(UUID userId){
        User loggedUser = authService.getAutheticatedUser();
        User targetUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class));

        this.validateRoleHierarchy(loggedUser.getUserRole(), targetUser.getUserRole());

        userRepository.delete(targetUser);
    }

    private void validateRoleHierarchy(UserRole loggedUserRole, UserRole targetUserRole){
        if (loggedUserRole.equals(UserRole.ADMIN)) return;
        if (loggedUserRole.equals(UserRole.MANAGER) && !targetUserRole.equals(UserRole.ADMIN)) return;
        throw new BusinessRuleException(ExceptionCode.FORBIDDEN);
    }
}
