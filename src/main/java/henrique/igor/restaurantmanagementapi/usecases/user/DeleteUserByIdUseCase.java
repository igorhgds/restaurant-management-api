package henrique.igor.restaurantmanagementapi.usecases.user;

import henrique.igor.restaurantmanagementapi.entities.User;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.repositories.user.UserJpaRepository;
import henrique.igor.restaurantmanagementapi.services.AuthenticationContextService;
import henrique.igor.restaurantmanagementapi.util.ValidateRoleHierarchy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteUserByIdUseCase {

    private final UserJpaRepository userRepository;
    private final AuthenticationContextService authService;
    private final ValidateRoleHierarchy validateRoleHierarchy;

    public void execute(UUID userId) {
        User loggedUser = authService.getAutheticatedUser();
        User targetUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class));

        validateRoleHierarchy.execute(loggedUser.getUserRole(), targetUser.getUserRole());

        userRepository.delete(targetUser);
    }
}
