package henrique.igor.restaurantmanagementapi.usecases.table;

import henrique.igor.restaurantmanagementapi.entities.RestaurantTable;
import henrique.igor.restaurantmanagementapi.enums.TableStatus;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.repositories.table.RestaurantTableJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteTableByIdUseCaseTest {

    @Mock
    private RestaurantTableJpaRepository repository;

    @Mock
    private FindTableByIdUseCase findTableByIdUseCase;

    @InjectMocks
    private DeleteTableByIdUseCase useCase;

    @Test
    void execute_WhenNotOccupied_ShouldDelete() {
        UUID id = UUID.randomUUID();
        RestaurantTable table = new RestaurantTable();
        table.setStatus(TableStatus.AVAILABLE);

        when(findTableByIdUseCase.getEntityById(id)).thenReturn(table);

        useCase.execute(id);

        verify(repository).delete(table);
    }

    @Test
    void execute_WhenOccupied_ShouldThrowBusinessRuleException() {
        UUID id = UUID.randomUUID();
        RestaurantTable table = new RestaurantTable();
        table.setStatus(TableStatus.OCCUPIED);

        when(findTableByIdUseCase.getEntityById(id)).thenReturn(table);

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> useCase.execute(id));
        assertEquals(ExceptionCode.OPERATION_NOT_ALLOWED, exception.getCode());
        verify(repository, never()).delete(any(RestaurantTable.class));
    }
}
