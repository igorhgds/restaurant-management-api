package henrique.igor.restaurantmanagementapi.entities.dtos.order.request;

import henrique.igor.restaurantmanagementapi.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequestDTO(
        @NotNull(message = "Status is required")
        OrderStatus status
) {}
