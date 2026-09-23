package henrique.igor.restaurantmanagementapi.entities.dtos.menu.request;

import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record UpdateMenuRequestDTO(
        @Size(max = 50) String name,
        @Size(max = 500) String description,
        Boolean active,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}