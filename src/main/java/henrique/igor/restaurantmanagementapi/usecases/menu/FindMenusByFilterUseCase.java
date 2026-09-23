package henrique.igor.restaurantmanagementapi.usecases.menu;

import henrique.igor.restaurantmanagementapi.entities.Menu;
import henrique.igor.restaurantmanagementapi.entities.dtos.menu.request.FindMenusByFilterRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.menu.response.MenuResponseDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.pagination.PageableResponseDTO;
import henrique.igor.restaurantmanagementapi.mapper.menu.MenuStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.menu.MenuJpaRepository;
import henrique.igor.restaurantmanagementapi.repositories.menu.MenuSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindMenusByFilterUseCase {

    private final MenuJpaRepository menuRepository;
    private final MenuStructMapper mapper;

    public PageableResponseDTO<MenuResponseDTO> execute(FindMenusByFilterRequestDTO filters) {

        var pageable = PageRequest.of(
                filters.getPage(),
                filters.getLimit(),
                Sort.by("name").ascending()
        );

        var spec = MenuSpecs.byFilters(filters);

        Page<Menu> pageOfMenus = menuRepository.findAll(spec, pageable);

        Page<MenuResponseDTO> pageOfDTOs = pageOfMenus.map(mapper::toMenuResponseDTO);

        return PageableResponseDTO.from(pageOfDTOs);
    }
}