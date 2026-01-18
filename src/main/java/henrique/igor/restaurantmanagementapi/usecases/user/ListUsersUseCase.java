package henrique.igor.restaurantmanagementapi.usecases.user;

import henrique.igor.restaurantmanagementapi.entities.User;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.response.MinimalUserResponseDTO;
import henrique.igor.restaurantmanagementapi.enums.UserRole;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.mapper.user.UserStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.user.UserJpaRepository;
import henrique.igor.restaurantmanagementapi.services.AuthenticationContextService;
import henrique.igor.restaurantmanagementapi.util.ValidateRoleHierarchy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ListUsersUseCase {

    private final UserJpaRepository userRepository;
    private final UserStructMapper userMapper;
    private final AuthenticationContextService authService;
    private final ValidateRoleHierarchy validateRoleHierarchy;

    public List<MinimalUserResponseDTO> execute(){
        User loggedUser = authService.getAutheticatedUser();
        if (loggedUser == null) {
            throw new BusinessRuleException(ExceptionCode.UNAUTHORIZED);
        }

        UserRole loggedUserRole = loggedUser.getUserRole();

        List<UserRole> visibleRoles = Optional.ofNullable(validateRoleHierarchy.getVisibleRoles(loggedUserRole))
                .orElse(Collections.emptyList());

        if (visibleRoles.isEmpty())
            return Collections.emptyList();

        return Optional.ofNullable(userRepository.findByUserRoleIn(visibleRoles))
                .orElse(Collections.emptyList())
                .stream()
                .map(userMapper::toMinimalUserResponseDTO)
                .toList();
    }
}
