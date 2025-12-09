package henrique.igor.restaurantmanagementapi.usecases.user;

import henrique.igor.restaurantmanagementapi.entities.User;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.request.UpdateUserRequestDTO;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.repositories.user.UserJpaRepository;
import henrique.igor.restaurantmanagementapi.services.AuthenticationContextService;
import henrique.igor.restaurantmanagementapi.util.ValidateRoleHierarchy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserUseCase {

    private final UserJpaRepository userRepository;
    private final AuthenticationContextService authService;
    private final ValidateRoleHierarchy validateRoleHierarchy;

    @Transactional
    public void execute(UpdateUserRequestDTO request){
        User loggedUser = authService.getAutheticatedUser();

        User targetUser = userRepository.findByUserId(request.userId())
                .orElseThrow(() -> new EntityNotFoundException(User.class));

        validateRoleHierarchy.execute(loggedUser.getUserRole(), targetUser.getUserRole());

        if (request.userRole() != null) {
            targetUser.setUserRole(request.userRole());
        }
        if (request.isEnabled() != null) {
            targetUser.setEnabled(request.isEnabled());
        }
    }
}
