package henrique.igor.restaurantmanagementapi.mapper.user;

import henrique.igor.restaurantmanagementapi.entities.Dish;
import henrique.igor.restaurantmanagementapi.entities.dtos.dish.response.DishResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DishStructMapper {

    DishResponseDTO toDishResponseDTO(Dish dish);
}
