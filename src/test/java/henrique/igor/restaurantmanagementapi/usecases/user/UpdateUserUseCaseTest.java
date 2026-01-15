package henrique.igor.restaurantmanagementapi.usecases.user;

import henrique.igor.restaurantmanagementapi.entities.User;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.request.UpdateUserRequestDTO;
import henrique.igor.restaurantmanagementapi.enums.UserRole;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
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
import static org.mockito.Mockito.*;

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
    private User targetUser;
    private UUID targetUserId;

    @BeforeEach
    void setUp() {
        loggedUser = new User();
        loggedUser.setUserId(UUID.randomUUID());
        loggedUser.setUserRole(UserRole.ADMIN);

        targetUserId = UUID.randomUUID();
        targetUser = new User();
        targetUser.setUserId(targetUserId);
        targetUser.setUserRole(UserRole.MANAGER);
    }

    @Test
    void shouldUpdateEnabledStatus_WhenAdminUpdatesManager() {
        // Arrange
        when(authService.getAutheticatedUser()).thenReturn(loggedUser);
        when(userRepository.findByUserId(targetUserId)).thenReturn(Optional.of(targetUser));

        UpdateUserRequestDTO request = new UpdateUserRequestDTO(null, null, null, false);

        // Act
        updateUserUseCase.execute(request, targetUserId);

        // Assert
        assertFalse(targetUser.isEnabled());

        // Verifica se validou hierarquia (já que IDs são diferentes)
        verify(validateRoleHierarchy).execute(loggedUser.getUserRole(), targetUser.getUserRole());
    }

    @Test
    void shouldUpdateUserRole_AndValidateHierarchyTwice() {
        // Arrange
        when(authService.getAutheticatedUser()).thenReturn(loggedUser);
        when(userRepository.findByUserId(targetUserId)).thenReturn(Optional.of(targetUser));

        // Admin transformando Manager em Waiter
        UpdateUserRequestDTO request = new UpdateUserRequestDTO(null, null, UserRole.WAITER, null);

        // Act
        updateUserUseCase.execute(request, targetUserId);

        // Assert
        assertEquals(UserRole.WAITER, targetUser.getUserRole());

        // Verifica validação 1: Posso mexer no alvo?
        verify(validateRoleHierarchy).execute(loggedUser.getUserRole(), UserRole.MANAGER);

        // Verifica validação 2: Posso dar esse cargo novo?
        verify(validateRoleHierarchy).execute(loggedUser.getUserRole(), UserRole.WAITER);
    }

    @Test
    void shouldThrowException_WhenSelfDeactivationAttempt() {
        // ARRANGE
        // 1. O usuário precisa começar ATIVO para testarmos se ele NÃO foi desativado
        loggedUser.setEnabled(true);

        when(authService.getAutheticatedUser()).thenReturn(loggedUser);
        when(userRepository.findByUserId(loggedUser.getUserId())).thenReturn(Optional.of(loggedUser));

        // Tenta desativar (isEnabled = false)
        UpdateUserRequestDTO request = new UpdateUserRequestDTO(null, null, null, false);

        // ACT & ASSERT
        // Garante que o erro estoura
        assertThrows(BusinessRuleException.class, () ->
                updateUserUseCase.execute(request, loggedUser.getUserId())
        );

        // Garante que o usuário CONTINUA ativo (o status não mudou)
        assertTrue(loggedUser.isEnabled());
    }

    @Test
    void shouldUpdateSelfInfo_WithoutCallingHierarchyCheck() {
        // Arrange - Usuário editando o próprio nome
        when(authService.getAutheticatedUser()).thenReturn(loggedUser);
        when(userRepository.findByUserId(loggedUser.getUserId())).thenReturn(Optional.of(loggedUser));

        UpdateUserRequestDTO request = new UpdateUserRequestDTO("Novo Nome", null, null, null);

        // Act
        updateUserUseCase.execute(request, loggedUser.getUserId());

        // Assert
        assertEquals("Novo Nome", loggedUser.getUsername());

        // IMPORTANTE: Como é ele mesmo, NÃO deve chamar o validador de hierarquia
        verify(validateRoleHierarchy, never()).execute(any(), any());
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(authService.getAutheticatedUser()).thenReturn(loggedUser);
        when(userRepository.findByUserId(targetUserId)).thenReturn(Optional.empty());

        UpdateUserRequestDTO request = new UpdateUserRequestDTO(null, null, UserRole.ADMIN, true);

        assertThrows(EntityNotFoundException.class, () ->
                updateUserUseCase.execute(request, targetUserId)
        );
    }
}