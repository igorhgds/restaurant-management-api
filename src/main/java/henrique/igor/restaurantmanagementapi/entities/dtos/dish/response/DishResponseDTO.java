package henrique.igor.restaurantmanagementapi.entities.dtos.dish.response;

import henrique.igor.restaurantmanagementapi.entities.Dish;

import java.math.BigDecimal;
import java.util.UUID;

public record DishResponseDTO(
        UUID dishId,
        String name,
        BigDecimal price,
        String description
) {
    public DishResponseDTO(Dish dish) {
        this(
                dish.getDishId(),
                dish.getName(),
                dish.getPrice(),
                dish.getDescription()
        );
    }
}