package henrique.igor.restaurantmanagementapi.usecases.user;

import henrique.igor.restaurantmanagementapi.entities.User;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.response.MinimalUserResponseDTO;
import henrique.igor.restaurantmanagementapi.enums.UserRole;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.mapper.user.UserStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.user.UserJpaRepository;
import henrique.igor.restaurantmanagementapi.services.AuthenticationContextService;
import henrique.igor.restaurantmanagementapi.util.ValidateRoleHierarchy;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class FindUserByIdUseCaseTest {

    @Mock
    private UserJpaRepository userRepository;
    @Mock
    private UserStructMapper userMapper;
    @Mock
    private AuthenticationContextService authService;
    @Mock
    private ValidateRoleHierarchy validateRoleHierarchy;

    @InjectMocks
    private FindUserByIdUseCase findUserByIdUseCase;

    private UUID userId;
    private User user;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User();
        user.setUserId(userId);
        user.setUserRole(UserRole.WAITER);
    }

    @Test
    @DisplayName("An exception should be thrown when the user is not found.")
    void shouldThrowExceptionWhenUserNotFound(){
        when(authService.getAutheticatedUser()).thenReturn(user);
        when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> findUserByIdUseCase.execute(userId));

        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("Should return the user without validating the hierarchy when the user searches for themselves.")
    void shouldReturnUserWithoutValidatingHierarchyWhenSearchingSelf(){
        when(authService.getAutheticatedUser()).thenReturn(user);
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(userMapper.toMinimalUserResponseDTO(user)).thenReturn(new MinimalUserResponseDTO("username", UserRole.WAITER));

        var result = findUserByIdUseCase.execute(userId);

        assertNotNull(result);

        verifyNoInteractions(validateRoleHierarchy);
    }

    @Test
    @DisplayName("Hierarchy must be validated when a user searches for another user.")
    void shouldValidateHierarchyWhenSearchingOtherUser() {
        User loggedUser = new User();
        loggedUser.setUserId(UUID.randomUUID());
        loggedUser.setUserRole(UserRole.MANAGER);

        when(authService.getAutheticatedUser()).thenReturn(loggedUser);
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(userMapper.toMinimalUserResponseDTO(user)).thenReturn(new MinimalUserResponseDTO("username", UserRole.WAITER));

        findUserByIdUseCase.execute(userId);

        verify(validateRoleHierarchy, times(1))
                .execute(loggedUser.getUserRole(), user.getUserRole());
    }

    @Test
    @DisplayName("An exception should be thrown when the role hierarchy is invalid.")
    void shouldThrowExceptionWhenRoleHierarchyIsInvalid() {
        User loggedUser = new User();
        loggedUser.setUserId(UUID.randomUUID());
        loggedUser.setUserRole(UserRole.WAITER);

        User targetUser = new User();
        targetUser.setUserId(userId);
        targetUser.setUserRole(UserRole.ADMIN);

        when(authService.getAutheticatedUser()).thenReturn(loggedUser);
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(targetUser));


        doThrow(new BusinessRuleException(ExceptionCode.FORBIDDEN))
                .when(validateRoleHierarchy)
                .execute(loggedUser.getUserRole(), targetUser.getUserRole());

        BusinessRuleException exception = assertThrows(BusinessRuleException.class,
                () -> findUserByIdUseCase.execute(userId));

        Assertions.assertEquals(ExceptionCode.FORBIDDEN, exception.getCode());

        verifyNoInteractions(userMapper);
    }
}