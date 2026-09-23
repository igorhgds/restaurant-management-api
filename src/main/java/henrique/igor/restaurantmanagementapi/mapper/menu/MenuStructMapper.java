package henrique.igor.restaurantmanagementapi.mapper.menu;

import henrique.igor.restaurantmanagementapi.entities.Menu;
import henrique.igor.restaurantmanagementapi.entities.dtos.menu.response.MenuResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuStructMapper {

    MenuResponseDTO toMenuResponseDTO(Menu menu);
}