package henrique.igor.restaurantmanagementapi.usecases.table;

import henrique.igor.restaurantmanagementapi.entities.RestaurantTable;
import henrique.igor.restaurantmanagementapi.entities.dtos.table.request.CreateTableRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.table.response.TableResponseDTO;
import henrique.igor.restaurantmanagementapi.enums.TableLocation;
import henrique.igor.restaurantmanagementapi.enums.TableStatus;
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
class CreateTableUseCaseTest {

    @Mock
    private RestaurantTableJpaRepository repository;

    @Mock
    private TableStructMapper mapper;

    @InjectMocks
    private CreateTableUseCase useCase;

    @Test
    void execute_WhenNumberDoesNotExist_ShouldCreateTable() {
        // Arrange
        CreateTableRequestDTO dto = new CreateTableRequestDTO(1, 4, TableLocation.INDOOR);
        RestaurantTable entity = new RestaurantTable();
        entity.setNumber(1);
        entity.setCapacity(4);
        entity.setLocation(TableLocation.INDOOR);

        TableResponseDTO responseDTO = new TableResponseDTO();
        responseDTO.setId(UUID.randomUUID());
        responseDTO.setNumber(1);
        responseDTO.setCapacity(4);
        responseDTO.setLocation(TableLocation.INDOOR);
        responseDTO.setStatus(TableStatus.AVAILABLE);

        when(repository.existsByNumber(dto.getNumber())).thenReturn(false);
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(any(RestaurantTable.class))).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(responseDTO);

        // Act
        TableResponseDTO result = useCase.execute(dto);

        // Assert
        assertNotNull(result);
        assertEquals(TableStatus.AVAILABLE, result.getStatus());
        verify(repository).save(entity);
    }

    @Test
    void execute_WhenNumberExists_ShouldThrowBusinessRuleException() {
        // Arrange
        CreateTableRequestDTO dto = new CreateTableRequestDTO(1, 4, TableLocation.INDOOR);
        when(repository.existsByNumber(dto.getNumber())).thenReturn(true);

        // Act & Assert
        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> useCase.execute(dto));
        assertEquals(henrique.igor.restaurantmanagementapi.errors.ExceptionCode.DUPLICATED_RESOURCE, exception.getCode());
        verify(repository, never()).save(any());
    }
}
