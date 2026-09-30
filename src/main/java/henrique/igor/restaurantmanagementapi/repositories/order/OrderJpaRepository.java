package henrique.igor.restaurantmanagementapi.repositories.order;

import henrique.igor.restaurantmanagementapi.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;
import henrique.igor.restaurantmanagementapi.entities.RestaurantTable;
import henrique.igor.restaurantmanagementapi.enums.OrderStatus;

@Repository
public interface OrderJpaRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {
    List<Order> findByTableAndStatusNot(RestaurantTable table, OrderStatus status);
}
