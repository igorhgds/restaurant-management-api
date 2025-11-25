package henrique.igor.restaurantmanagementapi.rest.controllers.auth;

import henrique.igor.restaurantmanagementapi.entities.dtos.auth.request.ActivateAccountRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.auth.request.LoginRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.auth.response.LoginResponseDTO;
import henrique.igor.restaurantmanagementapi.usecases.auth.ActivateAccountUseCase;
import henrique.igor.restaurantmanagementapi.usecases.auth.LoginUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ActivateAccountUseCase activateAccountUseCase;
    private final LoginUseCase loginUseCase;

    @PatchMapping("/activate")
    public void activateAccount(@RequestBody @Valid ActivateAccountRequestDTO request) {
        activateAccountUseCase.activateAccount(request);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        LoginResponseDTO response = loginUseCase.login(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
