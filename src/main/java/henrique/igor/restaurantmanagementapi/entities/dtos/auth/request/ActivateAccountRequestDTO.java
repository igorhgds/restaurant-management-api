package henrique.igor.restaurantmanagementapi.entities.dtos.auth.request;

import jakarta.validation.constraints.NotBlank;

public record ActivateAccountRequestDTO(
        @NotBlank String email,
        @NotBlank String passwordRecoveryCode,
        @NotBlank String password
) {
}
