package henrique.igor.restaurantmanagementapi.repositories.image;

import henrique.igor.restaurantmanagementapi.entities.DishImage;
import henrique.igor.restaurantmanagementapi.entities.DishImageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DishImageJpaRepository extends JpaRepository<DishImage, DishImageId> {
    List<DishImage> findByDish_DishId(UUID dishId);
}
