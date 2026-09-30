package henrique.igor.restaurantmanagementapi.usecases.table;

import henrique.igor.restaurantmanagementapi.entities.RestaurantTable;
import henrique.igor.restaurantmanagementapi.entities.dtos.table.request.CreateTableRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.table.response.TableResponseDTO;
import henrique.igor.restaurantmanagementapi.enums.TableStatus;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.mapper.table.TableStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.table.RestaurantTableJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTableUseCase {

    private final RestaurantTableJpaRepository tableRepository;
    private final TableStructMapper mapper;

    public TableResponseDTO execute(CreateTableRequestDTO dto) {
        if (tableRepository.existsByNumber(dto.getNumber())) {
            throw new BusinessRuleException(ExceptionCode.DUPLICATED_RESOURCE, "table.number.duplicate");
        }

        RestaurantTable table = mapper.toEntity(dto);
        table.setStatus(TableStatus.AVAILABLE);

        return mapper.toDTO(tableRepository.save(table));
    }
}
