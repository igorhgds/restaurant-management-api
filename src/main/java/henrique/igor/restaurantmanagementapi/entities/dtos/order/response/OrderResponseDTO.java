package henrique.igor.restaurantmanagementapi.entities.dtos.order.response;

import henrique.igor.restaurantmanagementapi.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponseDTO(
        UUID id,
        OrderStatus status,
        BigDecimal totalPrice,
        UUID tableId,
        UUID waiterId,
        List<OrderItemResponseDTO> items,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
