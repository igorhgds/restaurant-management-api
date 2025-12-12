package henrique.igor.restaurantmanagementapi.usecases.user;

import henrique.igor.restaurantmanagementapi.entities.User;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.request.CreateUserRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.response.UserResponseDTO;
import henrique.igor.restaurantmanagementapi.enums.UserRole;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.mapper.user.UserStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.user.UserJpaRepository;
import henrique.igor.restaurantmanagementapi.services.*;
import henrique.igor.restaurantmanagementapi.util.ValidateRoleHierarchy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateUserUseCaseTest {

    @Mock
    private UserJpaRepository userRepository;
    @Mock
    private RandomCodeService randomCodeService;
    @Mock
    private UserStructMapper userMapper;
    @Mock
    private EmailService emailService;
    @Mock
    private AuthenticationContextService authService;
    @Mock
    private ValidateRoleHierarchy validateRoleHierarchy;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    private User loggedAdmin;
    private CreateUserRequestDTO request;

    @BeforeEach
    void setUp() {
        loggedAdmin = new User();
        loggedAdmin.setUserRole(UserRole.ADMIN);

        request = new CreateUserRequestDTO("igor", "igor@email.com", UserRole.MANAGER);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        when(authService.getAutheticatedUser()).thenReturn(loggedAdmin);
        when(userRepository.existsByUsernameOrEmail("igor", "igor@email.com")).thenReturn(false);
        when(randomCodeService.generate(6)).thenReturn("123456");
        doNothing().when(validateRoleHierarchy).execute(any(), any());

        User mappedUser = new User();
        mappedUser.setUsername(request.username());
        mappedUser.setEmail(request.email());
        mappedUser.setUserRole(request.userRole());

        when(userMapper.toEntity(request)).thenReturn(mappedUser);
        when(userRepository.save(mappedUser)).thenReturn(mappedUser);

        UUID fakeId = UUID.randomUUID();
        LocalDateTime fakeCreatedAt = LocalDateTime.now();

        UserResponseDTO responseDTO = new UserResponseDTO(
                fakeId,
                "igor",
                "igor@email.com",
                UserRole.MANAGER,
                fakeCreatedAt);
        when(userMapper.toUserResponseDTO(mappedUser)).thenReturn(responseDTO);

        UserResponseDTO result = createUserUseCase.execute(request);

        assertEquals("igor", result.username());
        assertEquals("igor@email.com", result.email());
        verify(emailService).sendActivationEmail("igor@email.com", "123456");
        verify(userRepository).save(mappedUser);
    }

    @Test
    void shouldThrowExceptionWhenUsernameOrEmailDuplicated() {
        when(authService.getAutheticatedUser()).thenReturn(loggedAdmin);
        when(userRepository.existsByUsernameOrEmail("igor", "igor@email.com")).thenReturn(true);
        doNothing().when(validateRoleHierarchy).execute(any(), any());

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> createUserUseCase.execute(request)
        );

        assertEquals("user.username.or.email.duplicate", exception.getDetails());
    }

    @Test
    void shouldThrowWhenRoleHierarchyInvalid() {
        User manager = new User();
        manager.setUserRole(UserRole.MANAGER);

        when(authService.getAutheticatedUser()).thenReturn(manager);

        CreateUserRequestDTO adminRequest = new CreateUserRequestDTO("adminUser", "admin@example.com", UserRole.ADMIN);

        doThrow(new BusinessRuleException(ExceptionCode.FORBIDDEN))
                .when(validateRoleHierarchy)
                .execute(UserRole.MANAGER, UserRole.ADMIN);

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> createUserUseCase.execute(adminRequest));

        assertEquals(ExceptionCode.FORBIDDEN, ex.getCode());
    }

    @Test
    void shouldAllowManagerToCreateWaiter() {
        CreateUserRequestDTO request = new CreateUserRequestDTO(
                "igor", "igor@email.com", UserRole.WAITER);
        User manager = new User();
        manager.setUserRole(UserRole.MANAGER);

        when(authService.getAutheticatedUser()).thenReturn(manager);
        doNothing().when(validateRoleHierarchy).execute(any(), any());
        when(userRepository.existsByUsernameOrEmail("igor", "igor@email.com")).thenReturn(false);
        when(randomCodeService.generate(6)).thenReturn("XYZ789");

        User mappedUser = new User();
        mappedUser.setUsername(request.username());
        mappedUser.setEmail(request.email());
        mappedUser.setUserRole(request.userRole());
        when(userMapper.toEntity(request)).thenReturn(mappedUser);
        when(userRepository.save(mappedUser)).thenReturn(mappedUser);

        UUID fakeId = UUID.randomUUID();
        LocalDateTime fakeCreatedAt = LocalDateTime.now();

        UserResponseDTO responseDTO = new UserResponseDTO(
                fakeId,
                "igor",
                "igor@email.com",
                UserRole.WAITER,
                fakeCreatedAt);
        when(userMapper.toUserResponseDTO(mappedUser)).thenReturn(responseDTO);

        UserResponseDTO result = createUserUseCase.execute(request);

        assertEquals("igor", result.username());
        verify(emailService).sendActivationEmail("igor@email.com", "XYZ789");
        verify(validateRoleHierarchy).execute(UserRole.MANAGER, UserRole.WAITER);

    }
}
