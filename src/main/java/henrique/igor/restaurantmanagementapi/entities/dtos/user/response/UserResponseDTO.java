package henrique.igor.restaurantmanagementapi.entities.dtos.user.response;

import henrique.igor.restaurantmanagementapi.enums.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO(
        UUID userId,
        String username,
        String email,
        UserRole userRole,
        LocalDateTime createdAt) {
}
