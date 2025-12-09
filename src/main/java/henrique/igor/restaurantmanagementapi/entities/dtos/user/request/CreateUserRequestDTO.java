package henrique.igor.restaurantmanagementapi.entities.dtos.user.request;

import henrique.igor.restaurantmanagementapi.enums.UserRole;
import jakarta.validation.constraints.*;

public record CreateUserRequestDTO(
        @NotBlank String username,
        @NotBlank @Email String email,
        @NotNull UserRole userRole
) {
}
