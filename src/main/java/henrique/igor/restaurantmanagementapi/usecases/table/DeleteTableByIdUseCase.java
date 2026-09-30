package henrique.igor.restaurantmanagementapi.usecases.table;

import henrique.igor.restaurantmanagementapi.entities.RestaurantTable;
import henrique.igor.restaurantmanagementapi.enums.TableStatus;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.repositories.table.RestaurantTableJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteTableByIdUseCase {

    private final RestaurantTableJpaRepository tableRepository;
    private final FindTableByIdUseCase findTableByIdUseCase;

    public void execute(UUID id) {
        RestaurantTable table = findTableByIdUseCase.getEntityById(id);

        if (table.getStatus() == TableStatus.OCCUPIED) {
            throw new BusinessRuleException(ExceptionCode.OPERATION_NOT_ALLOWED, "table.delete.occupied");
        }

        tableRepository.delete(table);
    }
}
