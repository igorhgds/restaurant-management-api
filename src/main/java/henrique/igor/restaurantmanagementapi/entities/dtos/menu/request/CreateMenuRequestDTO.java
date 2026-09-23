package henrique.igor.restaurantmanagementapi.entities.dtos.menu.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CreateMenuRequestDTO(
        @NotBlank @Size(max = 50) String name,
        @Size(max = 500) String description,
        @NotBlank boolean active,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}