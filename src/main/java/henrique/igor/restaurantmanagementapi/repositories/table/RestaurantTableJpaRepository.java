package henrique.igor.restaurantmanagementapi.repositories.table;

import henrique.igor.restaurantmanagementapi.entities.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RestaurantTableJpaRepository extends JpaRepository<RestaurantTable, UUID>, JpaSpecificationExecutor<RestaurantTable> {
    boolean existsByNumber(Integer number);
    boolean existsByNumberAndTableIdNot(Integer number, UUID id);
}
