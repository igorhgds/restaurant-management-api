package henrique.igor.restaurantmanagementapi.usecases.table;

import henrique.igor.restaurantmanagementapi.entities.RestaurantTable;
import henrique.igor.restaurantmanagementapi.entities.dtos.table.request.UpdateTableRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.table.response.TableResponseDTO;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.mapper.table.TableStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.table.RestaurantTableJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateTableUseCase {

    private final RestaurantTableJpaRepository tableRepository;
    private final FindTableByIdUseCase findTableByIdUseCase;
    private final TableStructMapper mapper;

    public TableResponseDTO execute(UUID id, UpdateTableRequestDTO dto) {
        RestaurantTable table = findTableByIdUseCase.getEntityById(id);

        if (dto.getNumber() != null && !dto.getNumber().equals(table.getNumber())) {
            if (tableRepository.existsByNumberAndTableIdNot(dto.getNumber(), id)) {
                throw new BusinessRuleException(ExceptionCode.DUPLICATED_RESOURCE, "table.number.duplicate");
            }
            table.setNumber(dto.getNumber());
        }

        if (dto.getCapacity() != null) {
            table.setCapacity(dto.getCapacity());
        }

        if (dto.getLocation() != null) {
            table.setLocation(dto.getLocation());
        }

        return mapper.toDTO(tableRepository.save(table));
    }
}
