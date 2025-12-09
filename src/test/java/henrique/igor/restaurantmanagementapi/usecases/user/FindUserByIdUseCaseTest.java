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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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

    private User loggedAdmin;
    private UUID userId;
    private User targetUser;

    @BeforeEach
    void setUp() {
        loggedAdmin = new User();
        loggedAdmin.setUserRole(UserRole.ADMIN);

        userId = UUID.randomUUID();

        targetUser = new User();
        targetUser.setUserRole(UserRole.MANAGER);
    }

    @Test
    void shouldReturnUserWhenHierarchyValid(){
        when(authService.getAutheticatedUser()).thenReturn(loggedAdmin);
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(targetUser));
        doNothing().when(validateRoleHierarchy).execute(loggedAdmin.getUserRole(), targetUser.getUserRole());

        MinimalUserResponseDTO dto = new MinimalUserResponseDTO(
                "username", targetUser.getUserRole());
        when(userMapper.toMinimalUserResponseDTO(targetUser)).thenReturn(dto);

        MinimalUserResponseDTO result = findUserByIdUseCase.execute(userId);

        assertNotNull(result);
        assertEquals(dto, result);

        verify(userRepository).findByUserId(userId);
        verify(validateRoleHierarchy).execute(loggedAdmin.getUserRole(), targetUser.getUserRole());
        verify(userMapper).toMinimalUserResponseDTO(targetUser);
    }

    @Test
    void shouldThrowEntityNotFoundWhenUserDoesNotExist() {
        when(authService.getAutheticatedUser()).thenReturn(loggedAdmin);
        when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> findUserByIdUseCase.execute(userId));
    }

    @Test
    void shouldThrowBusinessRuleExceptionWhenHierarchyInvalid() {
        User managerUser = new User();
        managerUser.setUserRole(UserRole.MANAGER);

        User targetAdmin = new User();
        targetAdmin.setUserRole(UserRole.ADMIN);

        when(authService.getAutheticatedUser()).thenReturn(managerUser);
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(targetAdmin));
        doThrow(new BusinessRuleException(ExceptionCode.FORBIDDEN))
                .when(validateRoleHierarchy).execute(managerUser.getUserRole(), targetAdmin.getUserRole());

        BusinessRuleException ex = assertThrows(
                BusinessRuleException.class,
                () -> findUserByIdUseCase.execute(userId)
        );

        assertEquals(ExceptionCode.FORBIDDEN, ex.getCode());
    }
}
