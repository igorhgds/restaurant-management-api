package henrique.igor.restaurantmanagementapi.usecases.table;

import henrique.igor.restaurantmanagementapi.entities.dtos.table.response.TableResponseDTO;
import henrique.igor.restaurantmanagementapi.mapper.table.TableStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.table.RestaurantTableJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListTablesUseCase {

    private final RestaurantTableJpaRepository tableRepository;
    private final TableStructMapper mapper;

    public Page<TableResponseDTO> execute(Pageable pageable) {
        return tableRepository.findAll(pageable).map(mapper::toDTO);
    }
}
