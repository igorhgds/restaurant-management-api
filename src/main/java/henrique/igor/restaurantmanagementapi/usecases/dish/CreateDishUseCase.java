package henrique.igor.restaurantmanagementapi.usecases.dish;

import henrique.igor.restaurantmanagementapi.entities.Dish;
import henrique.igor.restaurantmanagementapi.entities.dtos.dish.request.CreateDishRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.dish.response.DishResponseDTO;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.repositories.dish.DishJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateDishUseCase {

    private final DishJpaRepository dishRepository;

    public DishResponseDTO execute(CreateDishRequestDTO request){
        if(dishRepository.existsByName(request.name())){
            throw new BusinessRuleException(ExceptionCode.DUPLICATED_RESOURCE);
        }

        Dish dish = new Dish();
        dish.setName(request.name());
        dish.setPrice(request.price());
        dish.setDescription(request.description());

        Dish saved = dishRepository.save(dish);
        return new DishResponseDTO(saved);
    }
}
