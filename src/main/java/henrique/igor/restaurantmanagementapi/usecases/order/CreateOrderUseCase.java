package henrique.igor.restaurantmanagementapi.usecases.order;

import henrique.igor.restaurantmanagementapi.entities.Dish;
import henrique.igor.restaurantmanagementapi.entities.Order;
import henrique.igor.restaurantmanagementapi.entities.OrderItem;
import henrique.igor.restaurantmanagementapi.entities.RestaurantTable;
import henrique.igor.restaurantmanagementapi.entities.User;
import henrique.igor.restaurantmanagementapi.entities.dtos.order.request.CreateOrderItemRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.order.request.CreateOrderRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.order.response.OrderResponseDTO;
import henrique.igor.restaurantmanagementapi.enums.OrderStatus;
import henrique.igor.restaurantmanagementapi.enums.TableStatus;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.mapper.order.OrderStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.dish.DishJpaRepository;
import henrique.igor.restaurantmanagementapi.repositories.order.OrderJpaRepository;
import henrique.igor.restaurantmanagementapi.repositories.table.RestaurantTableJpaRepository;
import henrique.igor.restaurantmanagementapi.services.AuthenticationContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCase {

    private final OrderJpaRepository orderRepository;
    private final RestaurantTableJpaRepository tableRepository;
    private final DishJpaRepository dishRepository;
    private final AuthenticationContextService authContextService;
    private final OrderStructMapper orderMapper;

    @Transactional
    public OrderResponseDTO execute(CreateOrderRequestDTO dto) {
        User waiter = authContextService.getAutheticatedUser();

        RestaurantTable table = tableRepository.findById(dto.tableId())
                .orElseThrow(() -> new EntityNotFoundException(RestaurantTable.class));

        if (table.getStatus() != TableStatus.AVAILABLE) {
            throw new BusinessRuleException(ExceptionCode.OPERATION_NOT_ALLOWED, "Table is not available");
        }

        table.setStatus(TableStatus.OCCUPIED);
        tableRepository.save(table);

        Order order = new Order();
        order.setTable(table);
        order.setWaiter(waiter);
        order.setStatus(OrderStatus.OPEN);

        List<OrderItem> items = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CreateOrderItemRequestDTO itemDto : dto.items()) {
            Dish dish = dishRepository.findById(itemDto.dishId())
                    .orElseThrow(() -> new EntityNotFoundException(Dish.class));

            if (!dish.isEnabled()) {
                throw new BusinessRuleException(ExceptionCode.OPERATION_NOT_ALLOWED, "Dish " + dish.getName() + " is not enabled");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setDish(dish);
            orderItem.setQuantity(itemDto.quantity());
            orderItem.setUnitPrice(dish.getPrice());

            BigDecimal subtotal = dish.getPrice().multiply(BigDecimal.valueOf(itemDto.quantity()));
            orderItem.setSubtotal(subtotal);
            orderItem.setOrder(order);

            items.add(orderItem);
            totalPrice = totalPrice.add(subtotal);
        }

        order.setItems(items);
        order.setPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);

        return orderMapper.toOrderResponseDTO(savedOrder);
    }
}
