package henrique.igor.restaurantmanagementapi.rest.controllers.auth;

import henrique.igor.restaurantmanagementapi.entities.dtos.auth.request.ActivateAccountRequestDTO;
import henrique.igor.restaurantmanagementapi.usecases.auth.ActivateAccountUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ActivateAccountUseCase activateAccountUseCase;

    @PatchMapping("/activate")
    public void activateAccount(@RequestBody @Valid ActivateAccountRequestDTO request) {
        activateAccountUseCase.activateAccount(request);
    }
}
