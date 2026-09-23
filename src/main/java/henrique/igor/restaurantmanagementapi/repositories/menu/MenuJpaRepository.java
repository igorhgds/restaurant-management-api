package henrique.igor.restaurantmanagementapi.repositories.menu;

import henrique.igor.restaurantmanagementapi.entities.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MenuJpaRepository extends JpaRepository<Menu, UUID>, JpaSpecificationExecutor<Menu> {

    boolean existsByName(String name);
}