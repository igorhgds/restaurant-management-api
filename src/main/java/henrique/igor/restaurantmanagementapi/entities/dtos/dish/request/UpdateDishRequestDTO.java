package henrique.igor.restaurantmanagementapi.entities.dtos.dish.request;

import henrique.igor.restaurantmanagementapi.enums.Category;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateDishRequestDTO(
        String name,
        @Positive(message = "The price must be greater than zero.") BigDecimal price,
        Category category,
        Boolean isEnabled,
        @Size(max = 500, message = "Description is too long.") String description
) {
}
