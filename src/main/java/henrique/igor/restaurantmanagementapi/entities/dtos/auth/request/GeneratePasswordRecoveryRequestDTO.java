package henrique.igor.restaurantmanagementapi.entities.dtos.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record GeneratePasswordRecoveryRequestDTO(
        @NotBlank @Email String email
) {
}
