package henrique.igor.restaurantmanagementapi.entities.dtos.menu.response;

import henrique.igor.restaurantmanagementapi.entities.Menu;

import java.time.LocalDateTime;
import java.util.UUID;

public record MenuResponseDTO(
        UUID menuId,
        String name,
        String description,
        boolean active,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public MenuResponseDTO(Menu menu) {
        this(
                menu.getMenuId(),
                menu.getName(),
                menu.getDescription(),
                menu.isActive(),
                menu.getStartDate(),
                menu.getEndDate(),
                menu.getCreatedAt(),
                menu.getUpdatedAt()
        );
    }
}