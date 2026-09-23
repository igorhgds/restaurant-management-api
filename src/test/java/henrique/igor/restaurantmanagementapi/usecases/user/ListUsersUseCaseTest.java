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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListUsersUseCaseTest {

    @Mock
    private UserJpaRepository userRepository;
    @Mock
    private UserStructMapper userMapper;
    @Mock
    private AuthenticationContextService authService;
    @Mock
    private ValidateRoleHierarchy validateRoleHierarchy;

    @InjectMocks
    private ListUsersUseCase listUsersUseCase;

    @Test
    @DisplayName("Should return an empty list and not call the repository when there are no visible roles.")
    void shouldReturnEmptyListWhenNoVisibleRoles() {
        User loggedUser = new User();
        loggedUser.setUserRole(UserRole.WAITER);

        when(authService.getAutheticatedUser()).thenReturn(loggedUser);
        when(validateRoleHierarchy.getVisibleRoles(UserRole.WAITER)).thenReturn(Collections.emptyList());

        List<MinimalUserResponseDTO> result = listUsersUseCase.execute();

        assertTrue(result.isEmpty());
        verifyNoInteractions(userRepository);
        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("Should return a list of DTOs when users are found.")
    void shouldReturnDtoListWhenUsersFound() {
        User loggedUser = new User();
        loggedUser.setUserRole(UserRole.ADMIN);

        List<UserRole> visibleRoles = List.of(UserRole.MANAGER, UserRole.WAITER);
        User foundUser = new User();
        foundUser.setUserId(UUID.randomUUID());

        when(authService.getAutheticatedUser()).thenReturn(loggedUser);
        when(validateRoleHierarchy.getVisibleRoles(UserRole.ADMIN)).thenReturn(visibleRoles);

        when(userRepository.findByUserRoleIn(visibleRoles)).thenReturn(List.of(foundUser));

        when(userMapper.toMinimalUserResponseDTO(foundUser)).thenReturn(new MinimalUserResponseDTO("Igor", UserRole.WAITER));

        List<MinimalUserResponseDTO> result = listUsersUseCase.execute();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Igor", result.getFirst().username());

        verify(userMapper, times(1)).toMinimalUserResponseDTO(foundUser);
    }

    @Test
    @DisplayName("Should throw a BusinessRuleException when there is no authenticated user.")
    void shouldThrowExceptionWhenNoAuthenticatedUser() {
        when(authService.getAutheticatedUser()).thenReturn(null);

        BusinessRuleException exception = assertThrows(BusinessRuleException.class,
                () -> listUsersUseCase.execute());

        assertEquals(ExceptionCode.UNAUTHORIZED, exception.getCode());

        verifyNoInteractions(validateRoleHierarchy);
    }

    @Test
    @DisplayName("Should return an empty list when the role hierarchy returns null.")
    void shouldReturnEmptyListWhenVisibleRolesIsNull() {
        User loggedUser = new User();
        loggedUser.setUserRole(UserRole.MANAGER);
        when(authService.getAutheticatedUser()).thenReturn(loggedUser);

        when(validateRoleHierarchy.getVisibleRoles(UserRole.MANAGER)).thenReturn(null);

        List<MinimalUserResponseDTO> result = listUsersUseCase.execute();

        assertTrue(result.isEmpty());

        verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("Should return an empty list when the repository returns null.")
    void shouldReturnEmptyListWhenRepositoryReturnsNull() {
        User loggedUser = new User();
        loggedUser.setUserRole(UserRole.ADMIN);
        List<UserRole> roles = List.of(UserRole.ADMIN);

        when(authService.getAutheticatedUser()).thenReturn(loggedUser);
        when(validateRoleHierarchy.getVisibleRoles(any())).thenReturn(roles);

        when(userRepository.findByUserRoleIn(roles)).thenReturn(null);

        List<MinimalUserResponseDTO> result = listUsersUseCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verifyNoInteractions(userMapper);
    }
}
