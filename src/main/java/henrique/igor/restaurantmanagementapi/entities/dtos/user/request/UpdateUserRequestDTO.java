package henrique.igor.restaurantmanagementapi.entities.dtos.user.request;

import henrique.igor.restaurantmanagementapi.enums.UserRole;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateUserRequestDTO(
        @NotNull UUID userId,
        UserRole userRole,
        Boolean isEnabled
) {
}
