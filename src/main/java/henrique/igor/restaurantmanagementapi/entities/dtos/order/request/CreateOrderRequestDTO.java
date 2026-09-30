package henrique.igor.restaurantmanagementapi.entities.dtos.order.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record CreateOrderRequestDTO(
        @NotNull(message = "Table ID is required")
        UUID tableId,

        @NotEmpty(message = "Items list cannot be empty")
        @Valid
        List<CreateOrderItemRequestDTO> items
) {}
