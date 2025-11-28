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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        when(userRepository.findByUsername("igor")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("igor@email.com")).thenReturn(Optional.empty());
        when(randomCodeService.generate(6)).thenReturn("123456");

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
    void shouldThrowExceptionWhenUsernameDuplicated() {
        when(authService.getAutheticatedUser()).thenReturn(loggedAdmin);
        when(userRepository.findByUsername("igor")).thenReturn(Optional.of(new User()));

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> createUserUseCase.execute(request)
        );
        assertTrue(exception.getDetails().toString().contains("username"));
    }

    @Test
    void shouldThrowExceptionWhenEmailDuplicated() {
        when(authService.getAutheticatedUser()).thenReturn(loggedAdmin);
        when(userRepository.findByEmail("igor@email.com")).thenReturn(Optional.of(new User()));

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> createUserUseCase.execute(request)
        );
        assertTrue(exception.getDetails().toString().contains("email"));
    }

    @Test
    void shouldThrowWhenRoleHierarchyInvalid() {
        User manager = new User();
        manager.setUserRole(UserRole.MANAGER);

        when(authService.getAutheticatedUser()).thenReturn(manager);

        CreateUserRequestDTO adminRequest = new CreateUserRequestDTO("adminUser", "admin@example.com", UserRole.ADMIN);

        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> createUserUseCase.execute(adminRequest));

        assertEquals(ExceptionCode.UNAUTHORIZED, ex.getCode());
    }

    @Test
    void shouldAllowManagerToCreateUserRole() {
        User manager = new User();
        manager.setUserRole(UserRole.MANAGER);

        when(authService.getAutheticatedUser()).thenReturn(manager);
        when(userRepository.findByUsername("igor")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("igor@email.com")).thenReturn(Optional.empty());
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
                UserRole.MANAGER,
                fakeCreatedAt);
        when(userMapper.toUserResponseDTO(mappedUser)).thenReturn(responseDTO);

        UserResponseDTO result = createUserUseCase.execute(request);

        assertEquals("igor", result.username());
        verify(emailService).sendActivationEmail("igor@email.com", "XYZ789");
    }
}
