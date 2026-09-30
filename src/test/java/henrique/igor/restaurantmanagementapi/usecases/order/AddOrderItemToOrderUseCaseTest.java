package henrique.igor.restaurantmanagementapi.usecases.order;

import henrique.igor.restaurantmanagementapi.entities.Dish;
import henrique.igor.restaurantmanagementapi.entities.Order;
import henrique.igor.restaurantmanagementapi.entities.OrderItem;
import henrique.igor.restaurantmanagementapi.entities.dtos.order.request.AddOrderItemRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.order.response.OrderResponseDTO;
import henrique.igor.restaurantmanagementapi.enums.OrderStatus;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.mapper.order.OrderStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.dish.DishJpaRepository;
import henrique.igor.restaurantmanagementapi.repositories.order.OrderJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddOrderItemToOrderUseCaseTest {

    @Mock
    private OrderJpaRepository orderRepository;

    @Mock
    private DishJpaRepository dishRepository;

    @Mock
    private OrderStructMapper orderMapper;

    @InjectMocks
    private AddOrderItemToOrderUseCase addOrderItemToOrderUseCase;

    private Order order;
    private Dish dish;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setOrderId(UUID.randomUUID());
        order.setStatus(OrderStatus.OPEN);
        order.setPrice(BigDecimal.ZERO);
        order.setItems(new ArrayList<>());

        dish = new Dish();
        dish.setDishId(UUID.randomUUID());
        dish.setPrice(new BigDecimal("15.00"));
        dish.setEnabled(true);
    }

    @Test
    void shouldAddItemSuccessfully() {
        AddOrderItemRequestDTO request = new AddOrderItemRequestDTO(dish.getDishId(), 2);

        when(orderRepository.findById(order.getOrderId())).thenReturn(Optional.of(order));
        when(dishRepository.findById(dish.getDishId())).thenReturn(Optional.of(dish));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        addOrderItemToOrderUseCase.execute(order.getOrderId(), request);

        verify(orderRepository).save(order);
        assertEquals(1, order.getItems().size());
        assertEquals(new BigDecimal("30.00"), order.getPrice());
    }

    @Test
    void shouldIncreaseQuantityIfItemExists() {
        OrderItem existingItem = new OrderItem();
        existingItem.setDish(dish);
        existingItem.setQuantity(1);
        existingItem.setUnitPrice(dish.getPrice());
        existingItem.setSubtotal(dish.getPrice());
        order.getItems().add(existingItem);
        order.setPrice(dish.getPrice());

        AddOrderItemRequestDTO request = new AddOrderItemRequestDTO(dish.getDishId(), 2);

        when(orderRepository.findById(order.getOrderId())).thenReturn(Optional.of(order));
        when(dishRepository.findById(dish.getDishId())).thenReturn(Optional.of(dish));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        addOrderItemToOrderUseCase.execute(order.getOrderId(), request);

        verify(orderRepository).save(order);
        assertEquals(1, order.getItems().size());
        assertEquals(3, order.getItems().get(0).getQuantity());
        assertEquals(new BigDecimal("45.00"), order.getPrice());
    }

    @Test
    void shouldThrowExceptionIfOrderNotOpen() {
        order.setStatus(OrderStatus.CLOSED);
        AddOrderItemRequestDTO request = new AddOrderItemRequestDTO(dish.getDishId(), 2);

        when(orderRepository.findById(order.getOrderId())).thenReturn(Optional.of(order));

        assertThrows(BusinessRuleException.class, () -> addOrderItemToOrderUseCase.execute(order.getOrderId(), request));
    }
}
