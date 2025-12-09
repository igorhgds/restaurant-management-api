package henrique.igor.restaurantmanagementapi.usecases.user;

import henrique.igor.restaurantmanagementapi.entities.User;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.response.MinimalUserResponseDTO;
import henrique.igor.restaurantmanagementapi.enums.UserRole;
import henrique.igor.restaurantmanagementapi.mapper.user.UserStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.user.UserJpaRepository;
import henrique.igor.restaurantmanagementapi.services.AuthenticationContextService;
import henrique.igor.restaurantmanagementapi.util.ValidateRoleHierarchy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListUsersUseCase {

    private final UserJpaRepository userRepository;
    private final UserStructMapper userMapper;
    private final AuthenticationContextService authService;
    private final ValidateRoleHierarchy validateRoleHierarchy;

    public List<MinimalUserResponseDTO> execute(){
        UserRole loggedUserRole = authService.getAutheticatedUser().getUserRole();

        List<UserRole> visibleRoles = validateRoleHierarchy.getVisibleRoles(loggedUserRole);

        if (visibleRoles.isEmpty())
            return Collections.emptyList();

        List<User> users = userRepository.findByUserRoleIn(visibleRoles);

        return users.stream()
                .map(userMapper::toMinimalUserResponseDTO)
                .collect(Collectors.toList());
    }
}
