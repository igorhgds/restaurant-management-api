package henrique.igor.restaurantmanagementapi.usecases.order;

import henrique.igor.restaurantmanagementapi.entities.Order;
import henrique.igor.restaurantmanagementapi.entities.RestaurantTable;
import henrique.igor.restaurantmanagementapi.entities.dtos.order.request.UpdateOrderStatusRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.order.response.OrderResponseDTO;
import henrique.igor.restaurantmanagementapi.enums.OrderStatus;
import henrique.igor.restaurantmanagementapi.enums.TableStatus;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.mapper.order.OrderStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.order.OrderJpaRepository;
import henrique.igor.restaurantmanagementapi.repositories.table.RestaurantTableJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateOrderStatusUseCaseTest {

    @Mock
    private OrderJpaRepository orderRepository;

    @Mock
    private RestaurantTableJpaRepository tableRepository;

    @Mock
    private OrderStructMapper orderMapper;

    @InjectMocks
    private UpdateOrderStatusUseCase updateOrderStatusUseCase;

    private Order order;
    private RestaurantTable table;

    @BeforeEach
    void setUp() {
        table = new RestaurantTable();
        table.setTableId(UUID.randomUUID());
        table.setStatus(TableStatus.OCCUPIED);

        order = new Order();
        order.setOrderId(UUID.randomUUID());
        order.setStatus(OrderStatus.OPEN);
        order.setTable(table);
    }

    @Test
    void shouldUpdateStatusSuccessfully() {
        UpdateOrderStatusRequestDTO request = new UpdateOrderStatusRequestDTO(OrderStatus.PREPARING);

        when(orderRepository.findById(order.getOrderId())).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderResponseDTO mockResponse = new OrderResponseDTO(order.getOrderId(), OrderStatus.PREPARING, null, null, null, null, null, null);
        when(orderMapper.toOrderResponseDTO(order)).thenReturn(mockResponse);

        OrderResponseDTO response = updateOrderStatusUseCase.execute(order.getOrderId(), request);

        assertEquals(OrderStatus.PREPARING, response.status());
        verify(orderRepository).save(order);
    }

    @Test
    void shouldThrowExceptionForInvalidTransition() {
        UpdateOrderStatusRequestDTO request = new UpdateOrderStatusRequestDTO(OrderStatus.DELIVERED);

        when(orderRepository.findById(order.getOrderId())).thenReturn(Optional.of(order));

        assertThrows(BusinessRuleException.class, () -> updateOrderStatusUseCase.execute(order.getOrderId(), request));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void shouldUpdateTableStatusWhenOrderClosed() {
        order.setStatus(OrderStatus.DELIVERED);
        UpdateOrderStatusRequestDTO request = new UpdateOrderStatusRequestDTO(OrderStatus.CLOSED);

        when(orderRepository.findById(order.getOrderId())).thenReturn(Optional.of(order));
        when(orderRepository.findByTableAndStatusNot(table, OrderStatus.CLOSED)).thenReturn(List.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        updateOrderStatusUseCase.execute(order.getOrderId(), request);

        assertEquals(TableStatus.AVAILABLE, table.getStatus());
        verify(tableRepository).save(table);
    }

    @Test
    void shouldNotUpdateTableStatusIfOtherOpenOrders() {
        order.setStatus(OrderStatus.DELIVERED);
        UpdateOrderStatusRequestDTO request = new UpdateOrderStatusRequestDTO(OrderStatus.CLOSED);

        Order otherOpenOrder = new Order();
        otherOpenOrder.setOrderId(UUID.randomUUID());
        otherOpenOrder.setStatus(OrderStatus.OPEN);

        when(orderRepository.findById(order.getOrderId())).thenReturn(Optional.of(order));
        when(orderRepository.findByTableAndStatusNot(table, OrderStatus.CLOSED)).thenReturn(List.of(order, otherOpenOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        updateOrderStatusUseCase.execute(order.getOrderId(), request);

        assertEquals(TableStatus.OCCUPIED, table.getStatus());
        verify(tableRepository, never()).save(any(RestaurantTable.class));
    }
}
