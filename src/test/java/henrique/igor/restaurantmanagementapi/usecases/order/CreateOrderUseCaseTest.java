package henrique.igor.restaurantmanagementapi.usecases.order;

import henrique.igor.restaurantmanagementapi.entities.Dish;
import henrique.igor.restaurantmanagementapi.entities.Order;
import henrique.igor.restaurantmanagementapi.entities.RestaurantTable;
import henrique.igor.restaurantmanagementapi.entities.User;
import henrique.igor.restaurantmanagementapi.entities.dtos.order.request.CreateOrderItemRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.order.request.CreateOrderRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.order.response.OrderResponseDTO;
import henrique.igor.restaurantmanagementapi.enums.OrderStatus;
import henrique.igor.restaurantmanagementapi.enums.TableStatus;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.mapper.order.OrderStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.dish.DishJpaRepository;
import henrique.igor.restaurantmanagementapi.repositories.order.OrderJpaRepository;
import henrique.igor.restaurantmanagementapi.repositories.table.RestaurantTableJpaRepository;
import henrique.igor.restaurantmanagementapi.services.AuthenticationContextService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseTest {

    @Mock
    private OrderJpaRepository orderRepository;

    @Mock
    private RestaurantTableJpaRepository tableRepository;

    @Mock
    private DishJpaRepository dishRepository;

    @Mock
    private AuthenticationContextService authContextService;

    @Mock
    private OrderStructMapper orderMapper;

    @InjectMocks
    private CreateOrderUseCase createOrderUseCase;

    private User waiter;
    private RestaurantTable table;
    private Dish dish;

    @BeforeEach
    void setUp() {
        waiter = new User();
        waiter.setUserId(UUID.randomUUID());

        table = new RestaurantTable();
        table.setTableId(UUID.randomUUID());
        table.setStatus(TableStatus.AVAILABLE);

        dish = new Dish();
        dish.setDishId(UUID.randomUUID());
        dish.setPrice(new BigDecimal("10.50"));
        dish.setEnabled(true);
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        CreateOrderRequestDTO request = new CreateOrderRequestDTO(table.getTableId(), List.of(new CreateOrderItemRequestDTO(dish.getDishId(), 2)));

        when(authContextService.getAutheticatedUser()).thenReturn(waiter);
        when(tableRepository.findById(table.getTableId())).thenReturn(Optional.of(table));
        when(dishRepository.findById(dish.getDishId())).thenReturn(Optional.of(dish));

        Order savedOrder = new Order();
        savedOrder.setOrderId(UUID.randomUUID());
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        OrderResponseDTO mockResponse = new OrderResponseDTO(savedOrder.getOrderId(), OrderStatus.OPEN, new BigDecimal("21.00"), table.getTableId(), waiter.getUserId(), List.of(), null, null);
        when(orderMapper.toOrderResponseDTO(savedOrder)).thenReturn(mockResponse);

        OrderResponseDTO response = createOrderUseCase.execute(request);

        assertNotNull(response);
        assertEquals(savedOrder.getOrderId(), response.id());
        assertEquals(TableStatus.OCCUPIED, table.getStatus());

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();

        assertEquals(OrderStatus.OPEN, capturedOrder.getStatus());
        assertEquals(waiter, capturedOrder.getWaiter());
        assertEquals(table, capturedOrder.getTable());
        assertEquals(new BigDecimal("21.00"), capturedOrder.getPrice());
        assertEquals(1, capturedOrder.getItems().size());
        assertEquals(dish, capturedOrder.getItems().get(0).getDish());
        assertEquals(2, capturedOrder.getItems().get(0).getQuantity());
        assertEquals(new BigDecimal("21.00"), capturedOrder.getItems().get(0).getSubtotal());
    }

    @Test
    void shouldThrowExceptionWhenTableNotFound() {
        CreateOrderRequestDTO request = new CreateOrderRequestDTO(UUID.randomUUID(), List.of());

        when(authContextService.getAutheticatedUser()).thenReturn(waiter);
        when(tableRepository.findById(request.tableId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> createOrderUseCase.execute(request));
    }

    @Test
    void shouldThrowExceptionWhenTableNotAvailable() {
        table.setStatus(TableStatus.OCCUPIED);
        CreateOrderRequestDTO request = new CreateOrderRequestDTO(table.getTableId(), List.of());

        when(authContextService.getAutheticatedUser()).thenReturn(waiter);
        when(tableRepository.findById(table.getTableId())).thenReturn(Optional.of(table));

        assertThrows(BusinessRuleException.class, () -> createOrderUseCase.execute(request));
    }

    @Test
    void shouldThrowExceptionWhenDishNotFound() {
        CreateOrderRequestDTO request = new CreateOrderRequestDTO(table.getTableId(), List.of(new CreateOrderItemRequestDTO(UUID.randomUUID(), 1)));

        when(authContextService.getAutheticatedUser()).thenReturn(waiter);
        when(tableRepository.findById(table.getTableId())).thenReturn(Optional.of(table));
        when(dishRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> createOrderUseCase.execute(request));
    }
}
