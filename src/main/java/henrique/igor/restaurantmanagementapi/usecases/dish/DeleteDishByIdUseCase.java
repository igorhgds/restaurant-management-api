package henrique.igor.restaurantmanagementapi.usecases.dish;

import henrique.igor.restaurantmanagementapi.entities.Dish;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.repositories.dish.DishJpaRepository;
import henrique.igor.restaurantmanagementapi.services.AuthenticationContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteDishByIdUseCase {

    private final DishJpaRepository dishRepository;
    private final AuthenticationContextService authService;

    public void execute(UUID dishId){
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new EntityNotFoundException(Dish.class));

        dishRepository.delete(dish);
    }
}
