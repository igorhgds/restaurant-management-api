package henrique.igor.restaurantmanagementapi.repositories.dish;

import henrique.igor.restaurantmanagementapi.entities.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DishJpaRepository extends JpaRepository<Dish, UUID>, JpaSpecificationExecutor<Dish> {

    boolean existsByName(String name);
}
