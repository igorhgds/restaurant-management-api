package henrique.igor.restaurantmanagementapi.usecases.table;

import henrique.igor.restaurantmanagementapi.entities.RestaurantTable;
import henrique.igor.restaurantmanagementapi.entities.dtos.table.request.UpdateTableStatusRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.table.response.TableResponseDTO;
import henrique.igor.restaurantmanagementapi.enums.TableStatus;
import henrique.igor.restaurantmanagementapi.mapper.table.TableStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.table.RestaurantTableJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateTableStatusUseCaseTest {

    @Mock
    private RestaurantTableJpaRepository repository;

    @Mock
    private FindTableByIdUseCase findTableByIdUseCase;

    @Mock
    private TableStructMapper mapper;

    @InjectMocks
    private UpdateTableStatusUseCase useCase;

    @Test
    void execute_ShouldUpdateStatus() {
        UUID id = UUID.randomUUID();
        UpdateTableStatusRequestDTO dto = new UpdateTableStatusRequestDTO(TableStatus.OCCUPIED);

        RestaurantTable table = new RestaurantTable();
        table.setTableId(id);
        table.setStatus(TableStatus.AVAILABLE);

        TableResponseDTO responseDTO = new TableResponseDTO();

        when(findTableByIdUseCase.getEntityById(id)).thenReturn(table);
        when(repository.save(table)).thenReturn(table);
        when(mapper.toDTO(table)).thenReturn(responseDTO);

        TableResponseDTO result = useCase.execute(id, dto);

        assertNotNull(result);
        assertEquals(TableStatus.OCCUPIED, table.getStatus());
        verify(repository).save(table);
    }
}
