package henrique.igor.restaurantmanagementapi.usecases.table;

import henrique.igor.restaurantmanagementapi.entities.RestaurantTable;
import henrique.igor.restaurantmanagementapi.entities.dtos.table.response.TableResponseDTO;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.mapper.table.TableStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.table.RestaurantTableJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindTableByIdUseCaseTest {

    @Mock
    private RestaurantTableJpaRepository repository;

    @Mock
    private TableStructMapper mapper;

    @InjectMocks
    private FindTableByIdUseCase useCase;

    @Test
    void execute_WhenFound_ShouldReturnTable() {
        UUID id = UUID.randomUUID();
        RestaurantTable table = new RestaurantTable();
        TableResponseDTO responseDTO = new TableResponseDTO();

        when(repository.findById(id)).thenReturn(Optional.of(table));
        when(mapper.toDTO(table)).thenReturn(responseDTO);

        TableResponseDTO result = useCase.execute(id);

        assertNotNull(result);
    }

    @Test
    void execute_WhenNotFound_ShouldThrowEntityNotFoundException() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> useCase.execute(id));
    }
}
