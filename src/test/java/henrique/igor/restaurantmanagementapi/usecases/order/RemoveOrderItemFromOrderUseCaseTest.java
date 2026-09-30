package henrique.igor.restaurantmanagementapi.usecases.order;

import henrique.igor.restaurantmanagementapi.entities.Order;
import henrique.igor.restaurantmanagementapi.entities.OrderItem;
import henrique.igor.restaurantmanagementapi.enums.OrderStatus;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.mapper.order.OrderStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.order.OrderItemJpaRepository;
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
class RemoveOrderItemFromOrderUseCaseTest {

    @Mock
    private OrderJpaRepository orderRepository;

    @Mock
    private OrderItemJpaRepository orderItemRepository;

    @Mock
    private OrderStructMapper orderMapper;

    @InjectMocks
    private RemoveOrderItemFromOrderUseCase removeOrderItemFromOrderUseCase;

    private Order order;
    private OrderItem item;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setOrderId(UUID.randomUUID());
        order.setStatus(OrderStatus.OPEN);
        order.setPrice(new BigDecimal("20.00"));
        order.setItems(new ArrayList<>());

        item = new OrderItem();
        item.setOrderItemId(UUID.randomUUID());
        item.setOrder(order);
        item.setSubtotal(new BigDecimal("20.00"));

        order.getItems().add(item);
    }

    @Test
    void shouldRemoveItemSuccessfully() {
        when(orderRepository.findById(order.getOrderId())).thenReturn(Optional.of(order));
        when(orderItemRepository.findById(item.getOrderItemId())).thenReturn(Optional.of(item));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        removeOrderItemFromOrderUseCase.execute(order.getOrderId(), item.getOrderItemId());

        verify(orderItemRepository).delete(item);
        verify(orderRepository).save(order);
        assertEquals(0, order.getItems().size());
        assertEquals(new BigDecimal("0.00"), order.getPrice());
    }

    @Test
    void shouldThrowExceptionIfOrderNotOpen() {
        order.setStatus(OrderStatus.CLOSED);

        when(orderRepository.findById(order.getOrderId())).thenReturn(Optional.of(order));

        assertThrows(BusinessRuleException.class, () -> removeOrderItemFromOrderUseCase.execute(order.getOrderId(), item.getOrderItemId()));
    }
}
