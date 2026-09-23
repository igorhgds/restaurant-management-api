package henrique.igor.restaurantmanagementapi.repositories.menu;

import henrique.igor.restaurantmanagementapi.entities.MenuDish;
import henrique.igor.restaurantmanagementapi.entities.MenuDishId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MenuDishJpaRepository extends JpaRepository<MenuDish, MenuDishId> {

    List<MenuDish> findByMenuId(UUID menuId);

    List<MenuDish> findByDishId(UUID dishId);

    boolean existsByMenuIdAndDishId(UUID menuId, UUID dishId);

    void deleteByMenuId(UUID menuId);

    void deleteByDishId(UUID dishId);
}