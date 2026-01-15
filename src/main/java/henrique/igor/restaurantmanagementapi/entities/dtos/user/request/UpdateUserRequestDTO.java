package henrique.igor.restaurantmanagementapi.entities.dtos.user.request;

import henrique.igor.restaurantmanagementapi.enums.UserRole;

public record UpdateUserRequestDTO(
        String username,
        String email,
        UserRole userRole,
        Boolean isEnabled
) {
}
