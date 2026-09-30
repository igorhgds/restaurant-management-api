package henrique.igor.restaurantmanagementapi.repositories.order;

import henrique.igor.restaurantmanagementapi.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderItemJpaRepository extends JpaRepository<OrderItem, UUID> {
}
