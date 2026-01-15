package henrique.igor.restaurantmanagementapi.usecases.dish;

import henrique.igor.restaurantmanagementapi.entities.Dish;
import henrique.igor.restaurantmanagementapi.entities.dtos.dish.request.UpdateDishRequestDTO;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.repositories.dish.DishJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateDishUseCase {

    private final DishJpaRepository dishRepository;

    @Transactional
    public void execute(UpdateDishRequestDTO request, UUID dishId) {
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new EntityNotFoundException(Dish.class));

        dish.updateInfo(
                request.name(),
                request.price(),
                request.category(),
                request.isEnabled(),
                request.description()
        );
    }
}
