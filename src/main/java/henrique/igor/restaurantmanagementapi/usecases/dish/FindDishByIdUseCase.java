package henrique.igor.restaurantmanagementapi.usecases.dish;

import henrique.igor.restaurantmanagementapi.entities.Dish;
import henrique.igor.restaurantmanagementapi.entities.dtos.dish.response.DishResponseDTO;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.mapper.user.DishStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.dish.DishJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindDishByIdUseCase {

    private final DishJpaRepository dishRepository;
    private final DishStructMapper mapper;

    public DishResponseDTO execute(UUID dishId){
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new EntityNotFoundException(Dish.class));

        return mapper.toDishResponseDTO(dish);
    }
}
