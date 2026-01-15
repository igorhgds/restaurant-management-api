package henrique.igor.restaurantmanagementapi.usecases.user;

import henrique.igor.restaurantmanagementapi.entities.User;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.request.UpdateUserRequestDTO;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.repositories.user.UserJpaRepository;
import henrique.igor.restaurantmanagementapi.services.AuthenticationContextService;
import henrique.igor.restaurantmanagementapi.util.ValidateRoleHierarchy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateUserUseCase {

    private final UserJpaRepository userRepository;
    private final AuthenticationContextService authService;
    private final ValidateRoleHierarchy validateRoleHierarchy;

    @Transactional
    public void execute(UpdateUserRequestDTO request, UUID userId){
        User loggedUser = authService.getAutheticatedUser();

        User targetUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class));

        if (!loggedUser.getUserId().equals(targetUser.getUserId())) {
            validateRoleHierarchy.execute(loggedUser.getUserRole(), targetUser.getUserRole());
        }

        if (request.userRole() != null) {
            validateRoleHierarchy.execute(loggedUser.getUserRole(), request.userRole());
        }

        if (loggedUser.getUserId().equals(targetUser.getUserId()) && Boolean.FALSE.equals(request.isEnabled())) {
            throw new BusinessRuleException(ExceptionCode.OPERATION_NOT_ALLOWED);
        }

        targetUser.updateInfo(
                request.username(),
                request.email(),
                request.userRole(),
                request.isEnabled()
        );
    }
}
