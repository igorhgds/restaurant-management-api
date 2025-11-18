package henrique.igor.restaurantmanagementapi.entities.dtos.user.request;

import henrique.igor.restaurantmanagementapi.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequestDTO(
        @NotBlank String username,
        @NotBlank String email,
        @NotNull UserRole userRole
) {
}
