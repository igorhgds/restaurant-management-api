package henrique.igor.restaurantmanagementapi.entities.dtos.order.response;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponseDTO(
        UUID id,
        UUID dishId,
        String dishName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {}
