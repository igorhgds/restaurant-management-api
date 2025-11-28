package henrique.igor.restaurantmanagementapi.usecases.auth;

import henrique.igor.restaurantmanagementapi.entities.User;
import henrique.igor.restaurantmanagementapi.entities.dtos.auth.request.LoginRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.auth.response.LoginResponseDTO;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.UnauthorizedException;
import henrique.igor.restaurantmanagementapi.security.dto.UserDetailsDTO;
import henrique.igor.restaurantmanagementapi.security.service.JwtTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginUseCaseTest {

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private LoginUseCase loginUseCase;

    @Mock
    private Authentication authentication;

    @Test
    void shouldLoginSuccessfully(){
        LoginRequestDTO request = new LoginRequestDTO("igor@email.com", "123456");

        User activateUser = new User();
        activateUser.setEmail("igor@email.com");
        activateUser.setEnabled(true);
        UserDetailsDTO userDetails = new UserDetailsDTO(activateUser);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtTokenService.generateToken(userDetails)).thenReturn("token-jwt-test");

        LoginResponseDTO response = loginUseCase.execute(request);

        assertNotNull(response);
        assertEquals("token-jwt-test", response.token());

        verify(authenticationManager).authenticate(any());
    }

    @Test
    void shouldThrowExceptionWhenUserIsDisabled(){
        LoginRequestDTO request = new LoginRequestDTO("igor@email.com", "123456");

        User disabledUser = new User();
        disabledUser.setEnabled(false);
        UserDetailsDTO userDetails = new UserDetailsDTO(disabledUser);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            loginUseCase.execute(request);
        });

        assertEquals(ExceptionCode.BAD_CREDENTIALS, exception.getCode());
        verify(jwtTokenService, never()).generateToken(any());
    }

    @Test
    void shouldThrowExceptionWhenCredentialsAreInvalid() {
        LoginRequestDTO request = new LoginRequestDTO("igor@email.com", "senha-errada");

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException(""));

        assertThrows(BadCredentialsException.class, () -> {
            loginUseCase.execute(request);
        });

        verify(jwtTokenService, never()).generateToken(any());
    }
}
