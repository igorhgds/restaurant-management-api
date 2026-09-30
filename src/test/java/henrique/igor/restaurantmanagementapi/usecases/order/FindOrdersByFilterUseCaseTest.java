package henrique.igor.restaurantmanagementapi.usecases.order;

import henrique.igor.restaurantmanagementapi.entities.Order;
import henrique.igor.restaurantmanagementapi.entities.dtos.order.request.FindOrdersByFilterRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.order.response.OrderResponseDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.pagination.PageableResponseDTO;
import henrique.igor.restaurantmanagementapi.mapper.order.OrderStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.order.OrderJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindOrdersByFilterUseCaseTest {

    @Mock
    private OrderJpaRepository orderRepository;

    @Mock
    private OrderStructMapper orderMapper;

    @InjectMocks
    private FindOrdersByFilterUseCase findOrdersByFilterUseCase;

    @Test
    void shouldReturnPageOfOrders() {
        FindOrdersByFilterRequestDTO filters = new FindOrdersByFilterRequestDTO();



        Order order = new Order();
        Page<Order> orderPage = new PageImpl<>(List.of(order));

        when(orderRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(orderPage);
        when(orderMapper.toOrderResponseDTO(any(Order.class))).thenReturn(null);

        PageableResponseDTO<OrderResponseDTO> response = findOrdersByFilterUseCase.execute(filters);

        assertNotNull(response);
    }
}
