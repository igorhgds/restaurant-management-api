package henrique.igor.restaurantmanagementapi.usecases.dish;

import henrique.igor.restaurantmanagementapi.entities.Dish;
import henrique.igor.restaurantmanagementapi.entities.dtos.dish.response.DishResponseDTO;
import henrique.igor.restaurantmanagementapi.mapper.dish.DishStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.dish.DishJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListDishesUseCase {

    private final DishJpaRepository dishRepository;
    private final DishStructMapper mapper;

    public List<DishResponseDTO> execute(){
        List<Dish> dishes = dishRepository.findAll();

        return dishes.stream()
                .map(mapper::toDishResponseDTO)
                .toList();
    }
}