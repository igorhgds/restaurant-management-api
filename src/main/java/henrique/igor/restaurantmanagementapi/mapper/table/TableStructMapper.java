package henrique.igor.restaurantmanagementapi.mapper.table;

import henrique.igor.restaurantmanagementapi.entities.RestaurantTable;
import henrique.igor.restaurantmanagementapi.entities.dtos.table.request.CreateTableRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.table.response.TableResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TableStructMapper {

    @Mapping(target = "tableId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    RestaurantTable toEntity(CreateTableRequestDTO dto);

    @Mapping(target = "id", source = "tableId")
    TableResponseDTO toDTO(RestaurantTable entity);
}
