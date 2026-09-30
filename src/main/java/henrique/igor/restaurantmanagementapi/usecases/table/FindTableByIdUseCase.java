package henrique.igor.restaurantmanagementapi.usecases.table;

import henrique.igor.restaurantmanagementapi.entities.RestaurantTable;
import henrique.igor.restaurantmanagementapi.entities.dtos.table.response.TableResponseDTO;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.mapper.table.TableStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.table.RestaurantTableJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindTableByIdUseCase {

    private final RestaurantTableJpaRepository tableRepository;
    private final TableStructMapper mapper;

    public TableResponseDTO execute(UUID id) {
        RestaurantTable table = getEntityById(id);
        return mapper.toDTO(table);
    }

    public RestaurantTable getEntityById(UUID id) {
        return tableRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(RestaurantTable.class));
    }
}
