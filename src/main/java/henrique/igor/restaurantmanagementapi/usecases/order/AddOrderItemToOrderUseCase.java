package henrique.igor.restaurantmanagementapi.usecases.order;

import henrique.igor.restaurantmanagementapi.entities.Dish;
import henrique.igor.restaurantmanagementapi.entities.Order;
import henrique.igor.restaurantmanagementapi.entities.OrderItem;
import henrique.igor.restaurantmanagementapi.entities.dtos.order.request.AddOrderItemRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.order.response.OrderResponseDTO;
import henrique.igor.restaurantmanagementapi.enums.OrderStatus;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.mapper.order.OrderStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.dish.DishJpaRepository;
import henrique.igor.restaurantmanagementapi.repositories.order.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddOrderItemToOrderUseCase {

    private final OrderJpaRepository orderRepository;
    private final DishJpaRepository dishRepository;
    private final OrderStructMapper orderMapper;

    @Transactional
    public OrderResponseDTO execute(UUID orderId, AddOrderItemRequestDTO dto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(Order.class));

        if (order.getStatus() != OrderStatus.OPEN) {
            throw new BusinessRuleException(ExceptionCode.OPERATION_NOT_ALLOWED, "Cannot add items to a non-OPEN order");
        }

        Dish dish = dishRepository.findById(dto.dishId())
                .orElseThrow(() -> new EntityNotFoundException(Dish.class));

        if (!dish.isEnabled()) {
            throw new BusinessRuleException(ExceptionCode.OPERATION_NOT_ALLOWED, "Dish " + dish.getName() + " is not enabled");
        }

        Optional<OrderItem> existingItemOpt = order.getItems().stream()
                .filter(item -> item.getDish().getDishId().equals(dto.dishId()))
                .findFirst();

        BigDecimal priceIncrease;

        if (existingItemOpt.isPresent()) {
            OrderItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + dto.quantity());
            BigDecimal additionalSubtotal = existingItem.getUnitPrice().multiply(BigDecimal.valueOf(dto.quantity()));
            existingItem.setSubtotal(existingItem.getSubtotal().add(additionalSubtotal));
            priceIncrease = additionalSubtotal;
        } else {
            OrderItem orderItem = new OrderItem();
            orderItem.setDish(dish);
            orderItem.setQuantity(dto.quantity());
            orderItem.setUnitPrice(dish.getPrice());

            BigDecimal subtotal = dish.getPrice().multiply(BigDecimal.valueOf(dto.quantity()));
            orderItem.setSubtotal(subtotal);
            orderItem.setOrder(order);

            order.getItems().add(orderItem);
            priceIncrease = subtotal;
        }

        order.setPrice(order.getPrice().add(priceIncrease));

        Order savedOrder = orderRepository.save(order);

        return orderMapper.toOrderResponseDTO(savedOrder);
    }
}
