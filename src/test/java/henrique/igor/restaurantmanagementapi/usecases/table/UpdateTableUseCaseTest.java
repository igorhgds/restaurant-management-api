package henrique.igor.restaurantmanagementapi.usecases.table;

import henrique.igor.restaurantmanagementapi.entities.RestaurantTable;
import henrique.igor.restaurantmanagementapi.entities.dtos.table.request.UpdateTableRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.table.response.TableResponseDTO;
import henrique.igor.restaurantmanagementapi.enums.TableLocation;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.mapper.table.TableStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.table.RestaurantTableJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateTableUseCaseTest {

    @Mock
    private RestaurantTableJpaRepository repository;

    @Mock
    private FindTableByIdUseCase findTableByIdUseCase;

    @Mock
    private TableStructMapper mapper;

    @InjectMocks
    private UpdateTableUseCase useCase;

    @Test
    void execute_WhenValidRequest_ShouldUpdateTable() {
        UUID id = UUID.randomUUID();
        UpdateTableRequestDTO dto = new UpdateTableRequestDTO(2, 6, TableLocation.OUTDOOR);

        RestaurantTable table = new RestaurantTable();
        table.setTableId(id);
        table.setNumber(1);
        table.setCapacity(4);
        table.setLocation(TableLocation.INDOOR);

        TableResponseDTO responseDTO = new TableResponseDTO();

        when(findTableByIdUseCase.getEntityById(id)).thenReturn(table);
        when(repository.existsByNumberAndTableIdNot(dto.getNumber(), id)).thenReturn(false);
        when(repository.save(table)).thenReturn(table);
        when(mapper.toDTO(table)).thenReturn(responseDTO);

        TableResponseDTO result = useCase.execute(id, dto);

        assertNotNull(result);
        assertEquals(2, table.getNumber());
        assertEquals(6, table.getCapacity());
        assertEquals(TableLocation.OUTDOOR, table.getLocation());
        verify(repository).save(table);
    }

    @Test
    void execute_WhenNumberAlreadyExists_ShouldThrowBusinessRuleException() {
        UUID id = UUID.randomUUID();
        UpdateTableRequestDTO dto = new UpdateTableRequestDTO(2, null, null);

        RestaurantTable table = new RestaurantTable();
        table.setTableId(id);
        table.setNumber(1);

        when(findTableByIdUseCase.getEntityById(id)).thenReturn(table);
        when(repository.existsByNumberAndTableIdNot(dto.getNumber(), id)).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> useCase.execute(id, dto));
        verify(repository, never()).save(any());
    }
}
