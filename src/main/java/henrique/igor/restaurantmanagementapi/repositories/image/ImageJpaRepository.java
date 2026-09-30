package henrique.igor.restaurantmanagementapi.repositories.image;

import henrique.igor.restaurantmanagementapi.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ImageJpaRepository extends JpaRepository<Image, UUID> {
}
