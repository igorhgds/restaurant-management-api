package henrique.igor.restaurantmanagementapi.rest.controllers;

import henrique.igor.restaurantmanagementapi.entities.dtos.auth.request.*;
import henrique.igor.restaurantmanagementapi.entities.dtos.auth.response.LoginResponseDTO;
import henrique.igor.restaurantmanagementapi.rest.specs.AuthControllerSpecs;
import henrique.igor.restaurantmanagementapi.usecases.auth.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerSpecs {

    private final GeneratePasswordRecoveryCodeUseCase generatePasswordRecoveryCodeUseCase;
    private final ActivateAccountUseCase activateAccountUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final LoginUseCase loginUseCase;

    @PatchMapping("/activate")
    public void activateAccount(@RequestBody @Valid ActivateAccountRequestDTO request) {
        activateAccountUseCase.execute(request);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        LoginResponseDTO response = loginUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/generate-password-recovery-code")
    public void generatePasswordRecovery(@RequestBody @Valid GeneratePasswordRecoveryRequestDTO request){
        generatePasswordRecoveryCodeUseCase.execute(request);
    }

    @PatchMapping("/change-password")
    public void changePassword(@RequestBody @Valid ChangePasswordRequestDTO request){
        changePasswordUseCase.execute(request);
    }
}
