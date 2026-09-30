package henrique.igor.restaurantmanagementapi.entities.dtos.order.request;

import henrique.igor.restaurantmanagementapi.entities.dtos.pagination.PageableRequestDTO;
import henrique.igor.restaurantmanagementapi.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class FindOrdersByFilterRequestDTO extends PageableRequestDTO {
    private OrderStatus status;
    private UUID tableId;
    private UUID waiterId;
    private LocalDate startDate;
    private LocalDate endDate;
}
