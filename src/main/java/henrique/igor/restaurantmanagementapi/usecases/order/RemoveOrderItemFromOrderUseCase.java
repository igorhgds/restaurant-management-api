package henrique.igor.restaurantmanagementapi.usecases.order;

import henrique.igor.restaurantmanagementapi.entities.Order;
import henrique.igor.restaurantmanagementapi.entities.OrderItem;
import henrique.igor.restaurantmanagementapi.entities.dtos.order.response.OrderResponseDTO;
import henrique.igor.restaurantmanagementapi.enums.OrderStatus;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.mapper.order.OrderStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.order.OrderItemJpaRepository;
import henrique.igor.restaurantmanagementapi.repositories.order.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RemoveOrderItemFromOrderUseCase {

    private final OrderJpaRepository orderRepository;
    private final OrderItemJpaRepository orderItemRepository;
    private final OrderStructMapper orderMapper;

    @Transactional
    public OrderResponseDTO execute(UUID orderId, UUID itemId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(Order.class));

        if (order.getStatus() != OrderStatus.OPEN) {
            throw new BusinessRuleException(ExceptionCode.OPERATION_NOT_ALLOWED, "Cannot remove items from a non-OPEN order");
        }

        OrderItem orderItem = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException(OrderItem.class));

        if (!orderItem.getOrder().getOrderId().equals(orderId)) {
             throw new BusinessRuleException(ExceptionCode.OPERATION_NOT_ALLOWED, "Item does not belong to this order");
        }

        order.setPrice(order.getPrice().subtract(orderItem.getSubtotal()));
        order.getItems().remove(orderItem);
        orderItemRepository.delete(orderItem);

        Order savedOrder = orderRepository.save(order);

        return orderMapper.toOrderResponseDTO(savedOrder);
    }
}
