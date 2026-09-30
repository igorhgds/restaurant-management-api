package henrique.igor.restaurantmanagementapi.usecases.table;

import henrique.igor.restaurantmanagementapi.entities.RestaurantTable;
import henrique.igor.restaurantmanagementapi.entities.dtos.table.request.UpdateTableStatusRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.table.response.TableResponseDTO;
import henrique.igor.restaurantmanagementapi.mapper.table.TableStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.table.RestaurantTableJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateTableStatusUseCase {

    private final RestaurantTableJpaRepository tableRepository;
    private final FindTableByIdUseCase findTableByIdUseCase;
    private final TableStructMapper mapper;

    public TableResponseDTO execute(UUID id, UpdateTableStatusRequestDTO dto) {
        RestaurantTable table = findTableByIdUseCase.getEntityById(id);

        table.setStatus(dto.getStatus());

        return mapper.toDTO(tableRepository.save(table));
    }
}
