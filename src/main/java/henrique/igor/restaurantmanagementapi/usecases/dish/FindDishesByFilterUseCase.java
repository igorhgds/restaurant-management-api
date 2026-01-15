package henrique.igor.restaurantmanagementapi.usecases.dish;

import henrique.igor.restaurantmanagementapi.entities.Dish;
import henrique.igor.restaurantmanagementapi.entities.dtos.dish.request.FindDishesByFilterRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.dish.response.DishResponseDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.pagination.PageableResponseDTO;
import henrique.igor.restaurantmanagementapi.mapper.dish.DishStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.dish.DishJpaRepository;
import henrique.igor.restaurantmanagementapi.repositories.dish.DishSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindDishesByFilterUseCase {

    private final DishJpaRepository dishRepository;
    private final DishStructMapper mapper;

    public PageableResponseDTO<DishResponseDTO> findByFilters(FindDishesByFilterRequestDTO filters) {

        var pageable = PageRequest.of(
                filters.getPage(),
                filters.getLimit(),
                Sort.by("name").ascending()
        );

        var spec = DishSpecs.byFilters(filters);

        Page<Dish> pageOfDishes = dishRepository.findAll(spec, pageable);

        Page<DishResponseDTO> pageOfDTOs = pageOfDishes.map(mapper::toDishResponseDTO);

        return PageableResponseDTO.from(pageOfDTOs);
    }
}
