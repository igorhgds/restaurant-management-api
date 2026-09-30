package henrique.igor.restaurantmanagementapi.usecases.order;

import henrique.igor.restaurantmanagementapi.entities.Order;
import henrique.igor.restaurantmanagementapi.entities.dtos.order.response.OrderResponseDTO;
import henrique.igor.restaurantmanagementapi.enums.OrderStatus;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.mapper.order.OrderStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.order.OrderJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindOrderByIdUseCaseTest {

    @Mock
    private OrderJpaRepository orderRepository;

    @Mock
    private OrderStructMapper orderMapper;

    @InjectMocks
    private FindOrderByIdUseCase findOrderByIdUseCase;

    @Test
    void shouldFindOrderByIdSuccessfully() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        order.setOrderId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.toOrderResponseDTO(order)).thenReturn(new OrderResponseDTO(orderId, OrderStatus.OPEN, null, null, null, null, null, null));

        OrderResponseDTO response = findOrderByIdUseCase.execute(orderId);

        assertNotNull(response);
    }

    @Test
    void shouldThrowExceptionIfOrderNotFound() {
        UUID orderId = UUID.randomUUID();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> findOrderByIdUseCase.execute(orderId));
    }
}
