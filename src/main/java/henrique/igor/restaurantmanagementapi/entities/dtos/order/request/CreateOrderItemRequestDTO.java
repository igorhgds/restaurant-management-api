package henrique.igor.restaurantmanagementapi.entities.dtos.order.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateOrderItemRequestDTO(
        @NotNull(message = "Dish ID is required")
        UUID dishId,

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity
) {}
