package henrique.igor.restaurantmanagementapi.rest.controllers;

import henrique.igor.restaurantmanagementapi.entities.dtos.order.request.AddOrderItemRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.order.request.CreateOrderRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.order.request.FindOrdersByFilterRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.order.request.UpdateOrderStatusRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.order.response.OrderResponseDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.pagination.PageableResponseDTO;
import henrique.igor.restaurantmanagementapi.usecases.order.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final AddOrderItemToOrderUseCase addOrderItemToOrderUseCase;
    private final RemoveOrderItemFromOrderUseCase removeOrderItemFromOrderUseCase;
    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;
    private final FindOrderByIdUseCase findOrderByIdUseCase;
    private final FindOrdersByFilterUseCase findOrdersByFilterUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('WAITER', 'MANAGER', 'ADMIN')")
    public OrderResponseDTO createOrder(@RequestBody @Valid CreateOrderRequestDTO dto) {
        return createOrderUseCase.execute(dto);
    }

    @PostMapping("/{orderId}/items")
    @PreAuthorize("hasAnyRole('WAITER', 'MANAGER', 'ADMIN')")
    public OrderResponseDTO addOrderItem(@PathVariable UUID orderId, @RequestBody @Valid AddOrderItemRequestDTO dto) {
        return addOrderItemToOrderUseCase.execute(orderId, dto);
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasAnyRole('WAITER', 'MANAGER', 'ADMIN')")
    public OrderResponseDTO removeOrderItem(@PathVariable UUID orderId, @PathVariable UUID itemId) {
        return removeOrderItemFromOrderUseCase.execute(orderId, itemId);
    }

    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasAnyRole('WAITER', 'MANAGER', 'ADMIN')")
    public OrderResponseDTO updateOrderStatus(@PathVariable UUID orderId, @RequestBody @Valid UpdateOrderStatusRequestDTO dto) {
        return updateOrderStatusUseCase.execute(orderId, dto);
    }

    @GetMapping("/{orderId}")
    public OrderResponseDTO getOrderById(@PathVariable UUID orderId) {
        return findOrderByIdUseCase.execute(orderId);
    }

    @GetMapping("/filter")
    public PageableResponseDTO<OrderResponseDTO> getOrdersByFilter(@ModelAttribute FindOrdersByFilterRequestDTO filters) {
        return findOrdersByFilterUseCase.execute(filters);
    }
}
