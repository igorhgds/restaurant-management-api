package henrique.igor.restaurantmanagementapi.usecases.table;

import henrique.igor.restaurantmanagementapi.entities.dtos.table.request.FindTablesByFilterRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.table.response.TableResponseDTO;
import henrique.igor.restaurantmanagementapi.mapper.table.TableStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.table.RestaurantTableJpaRepository;
import henrique.igor.restaurantmanagementapi.repositories.table.RestaurantTableSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindTablesByFilterUseCase {

    private final RestaurantTableJpaRepository tableRepository;
    private final TableStructMapper mapper;

    public Page<TableResponseDTO> execute(FindTablesByFilterRequestDTO filter, Pageable pageable) {
        return tableRepository.findAll(RestaurantTableSpecs.byFilter(filter), pageable).map(mapper::toDTO);
    }
}
