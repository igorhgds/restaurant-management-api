package henrique.igor.restaurantmanagementapi.entities.dtos.dish.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateDishRequestDTO(
        @NotBlank String name,
        @NotNull BigDecimal price,
        String description
) {
}
