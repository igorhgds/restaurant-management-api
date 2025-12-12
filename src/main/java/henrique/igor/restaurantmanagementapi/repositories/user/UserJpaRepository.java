package henrique.igor.restaurantmanagementapi.repositories.user;

import henrique.igor.restaurantmanagementapi.entities.User;
import henrique.igor.restaurantmanagementapi.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface UserJpaRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUserId(UUID userId);

    List<User> findByUserRoleIn(List<UserRole> roles);

    boolean existsByUsernameOrEmail(@NotBlank String username, @NotBlank @Email String email);
}
