package henrique.igor.restaurantmanagementapi.entities.dtos.dish.request;

import henrique.igor.restaurantmanagementapi.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateDishRequestDTO(
        @NotBlank String name,
        @NotNull BigDecimal price,
        @NotNull Category category,
        String description
) {
}
