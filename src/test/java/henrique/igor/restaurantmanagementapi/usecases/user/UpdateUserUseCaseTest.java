package henrique.igor.restaurantmanagementapi.usecases.user;

import henrique.igor.restaurantmanagementapi.entities.User;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.request.UpdateUserRequestDTO;
import henrique.igor.restaurantmanagementapi.enums.UserRole;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.repositories.user.UserJpaRepository;
import henrique.igor.restaurantmanagementapi.services.AuthenticationContextService;
import henrique.igor.restaurantmanagementapi.util.ValidateRoleHierarchy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateUserUseCaseTest {

    @Mock
    private UserJpaRepository userRepository;
    @Mock
    private AuthenticationContextService authService;
    @Mock
    private ValidateRoleHierarchy validateRoleHierarchy;

    @InjectMocks
    private UpdateUserUseCase updateUserUseCase;

    private User loggedUser;
    private UUID userId;
    private User targetUser;

    @BeforeEach
    void setUp() {
        loggedUser = new User();
        loggedUser.setUserRole(UserRole.ADMIN);

        userId = UUID.randomUUID();

        targetUser = new User();
        targetUser.setUserRole(UserRole.MANAGER);
    }

    @Test
    void shouldUpdateEnabledOnly(){
        when(authService.getAutheticatedUser()).thenReturn(loggedUser);
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(targetUser));

        UpdateUserRequestDTO request = new UpdateUserRequestDTO(userId, null, false);
        updateUserUseCase.execute(request);

        assertFalse(targetUser.isEnabled());
        verify(validateRoleHierarchy).execute(loggedUser.getUserRole(), targetUser.getUserRole());
    }

    @Test
    void shouldUpdateUserRoleOnly(){
        when(authService.getAutheticatedUser()).thenReturn(loggedUser);
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(targetUser));

        UpdateUserRequestDTO request = new UpdateUserRequestDTO(userId, UserRole.MANAGER, null);
        updateUserUseCase.execute(request);

        assertEquals(UserRole.MANAGER, targetUser.getUserRole());
        verify(validateRoleHierarchy).execute(loggedUser.getUserRole(), targetUser.getUserRole());
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());

        UpdateUserRequestDTO request = new UpdateUserRequestDTO(userId, UserRole.ADMIN, true);

        assertThrows(EntityNotFoundException.class, () -> updateUserUseCase.execute(request));
    }
}
