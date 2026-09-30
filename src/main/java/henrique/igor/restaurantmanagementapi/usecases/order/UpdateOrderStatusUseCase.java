package henrique.igor.restaurantmanagementapi.usecases.order;

import henrique.igor.restaurantmanagementapi.entities.Order;
import henrique.igor.restaurantmanagementapi.entities.RestaurantTable;
import henrique.igor.restaurantmanagementapi.entities.dtos.order.request.UpdateOrderStatusRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.order.response.OrderResponseDTO;
import henrique.igor.restaurantmanagementapi.enums.OrderStatus;
import henrique.igor.restaurantmanagementapi.enums.TableStatus;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.mapper.order.OrderStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.order.OrderJpaRepository;
import henrique.igor.restaurantmanagementapi.repositories.table.RestaurantTableJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateOrderStatusUseCase {

    private final OrderJpaRepository orderRepository;
    private final RestaurantTableJpaRepository tableRepository;
    private final OrderStructMapper orderMapper;

    @Transactional
    public OrderResponseDTO execute(UUID orderId, UpdateOrderStatusRequestDTO dto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(Order.class));

        OrderStatus currentStatus = order.getStatus();
        OrderStatus newStatus = dto.status();

        if (currentStatus == newStatus) {
            return orderMapper.toOrderResponseDTO(order);
        }

        validateTransition(currentStatus, newStatus);

        order.setStatus(newStatus);

        if (newStatus == OrderStatus.CLOSED || newStatus == OrderStatus.CANCELLED) {
            RestaurantTable table = order.getTable();
            List<Order> openOrdersForTable = orderRepository.findByTableAndStatusNot(table, OrderStatus.CLOSED);

            boolean hasOtherOpenOrders = openOrdersForTable.stream()
                .anyMatch(o -> !o.getOrderId().equals(orderId) && o.getStatus() != OrderStatus.CANCELLED);

            if (!hasOtherOpenOrders) {
                table.setStatus(TableStatus.AVAILABLE);
                tableRepository.save(table);
            }
        }

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toOrderResponseDTO(savedOrder);
    }

    private void validateTransition(OrderStatus current, OrderStatus next) {
        boolean isValid = switch (current) {
            case OPEN -> next == OrderStatus.PREPARING || next == OrderStatus.CANCELLED;
            case PREPARING -> next == OrderStatus.READY || next == OrderStatus.CANCELLED;
            case READY -> next == OrderStatus.DELIVERED || next == OrderStatus.CANCELLED;
            case DELIVERED -> next == OrderStatus.CLOSED;
            case CLOSED, CANCELLED -> false;
        };

        if (!isValid) {
            throw new BusinessRuleException(ExceptionCode.OPERATION_NOT_ALLOWED,
                "Invalid status transition from " + current + " to " + next);
        }
    }
}
