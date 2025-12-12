package henrique.igor.restaurantmanagementapi.repositories.dish;

import henrique.igor.restaurantmanagementapi.entities.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DishJpaRepository extends JpaRepository<Dish, UUID> {
    boolean existsByName(String name);
}
