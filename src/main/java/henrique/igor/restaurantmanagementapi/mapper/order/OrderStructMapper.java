package henrique.igor.restaurantmanagementapi.mapper.order;

import henrique.igor.restaurantmanagementapi.entities.Order;
import henrique.igor.restaurantmanagementapi.entities.OrderItem;
import henrique.igor.restaurantmanagementapi.entities.dtos.order.response.OrderItemResponseDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.order.response.OrderResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderStructMapper {

    @Mapping(target = "id", source = "orderId")
    @Mapping(target = "totalPrice", source = "price")
    @Mapping(target = "tableId", source = "table.tableId")
    @Mapping(target = "waiterId", source = "waiter.userId")
    OrderResponseDTO toOrderResponseDTO(Order order);

    @Mapping(target = "id", source = "orderItemId")
    @Mapping(target = "dishId", source = "dish.dishId")
    @Mapping(target = "dishName", source = "dish.name")
    OrderItemResponseDTO toOrderItemResponseDTO(OrderItem orderItem);

    default List<OrderItemResponseDTO> toOrderItemResponseDTOList(List<OrderItem> items) {
        if (items == null) {
            return null;
        }
        return items.stream()
                .map(this::toOrderItemResponseDTO)
                .collect(Collectors.toList());
    }
}
