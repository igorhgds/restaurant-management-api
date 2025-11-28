package henrique.igor.restaurantmanagementapi.usecases.auth;

import henrique.igor.restaurantmanagementapi.entities.dtos.auth.request.LoginRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.auth.response.LoginResponseDTO;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.UnauthorizedException;
import henrique.igor.restaurantmanagementapi.security.dto.UserDetailsDTO;
import henrique.igor.restaurantmanagementapi.security.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUseCase {
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;

    public LoginResponseDTO execute(LoginRequestDTO request){
        var usernamePassword = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        var auth = authenticationManager.authenticate(usernamePassword);
        var userDetails = (UserDetailsDTO) auth.getPrincipal();

        if (!userDetails.getUser().isEnabled())
            throw new UnauthorizedException(ExceptionCode.BAD_CREDENTIALS);

        var token = jwtTokenService.generateToken(userDetails);
        return new LoginResponseDTO(token);
    }
}
