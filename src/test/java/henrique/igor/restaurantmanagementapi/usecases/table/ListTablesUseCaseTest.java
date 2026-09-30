package henrique.igor.restaurantmanagementapi.usecases.table;

import henrique.igor.restaurantmanagementapi.entities.RestaurantTable;
import henrique.igor.restaurantmanagementapi.entities.dtos.table.response.TableResponseDTO;
import henrique.igor.restaurantmanagementapi.mapper.table.TableStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.table.RestaurantTableJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListTablesUseCaseTest {

    @Mock
    private RestaurantTableJpaRepository repository;

    @Mock
    private TableStructMapper mapper;

    @InjectMocks
    private ListTablesUseCase useCase;

    @Test
    void execute_ShouldReturnPagedTables() {
        Pageable pageable = PageRequest.of(0, 10);
        RestaurantTable table = new RestaurantTable();
        Page<RestaurantTable> page = new PageImpl<>(Collections.singletonList(table));

        TableResponseDTO responseDTO = new TableResponseDTO();

        when(repository.findAll(pageable)).thenReturn(page);
        when(mapper.toDTO(table)).thenReturn(responseDTO);

        Page<TableResponseDTO> result = useCase.execute(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
}
