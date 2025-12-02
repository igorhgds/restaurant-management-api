package henrique.igor.restaurantmanagementapi.entities.dtos.user.response;

import henrique.igor.restaurantmanagementapi.enums.UserRole;

public record MinimalUserResponseDTO(
        String username,
        UserRole userRole
) {
}
