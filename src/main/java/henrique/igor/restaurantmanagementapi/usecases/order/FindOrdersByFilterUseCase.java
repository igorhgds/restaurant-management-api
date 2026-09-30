package henrique.igor.restaurantmanagementapi.usecases.order;

import henrique.igor.restaurantmanagementapi.entities.Order;
import henrique.igor.restaurantmanagementapi.entities.dtos.order.request.FindOrdersByFilterRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.order.response.OrderResponseDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.pagination.PageableResponseDTO;
import henrique.igor.restaurantmanagementapi.mapper.order.OrderStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.order.OrderJpaRepository;
import henrique.igor.restaurantmanagementapi.repositories.order.OrderSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindOrdersByFilterUseCase {

    private final OrderJpaRepository orderRepository;
    private final OrderStructMapper orderMapper;

    public PageableResponseDTO<OrderResponseDTO> execute(FindOrdersByFilterRequestDTO filters) {

        var pageable = PageRequest.of(
                filters.getPage(),
                filters.getLimit(),
                Sort.by("createdAt").descending()
        );

        var spec = OrderSpecs.byFilters(filters);

        Page<Order> pageOfOrders = orderRepository.findAll(spec, pageable);

        Page<OrderResponseDTO> pageOfDTOs = pageOfOrders.map(orderMapper::toOrderResponseDTO);

        return PageableResponseDTO.from(pageOfDTOs);
    }
}
