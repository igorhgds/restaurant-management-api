package henrique.igor.restaurantmanagementapi.usecases.menu;

import henrique.igor.restaurantmanagementapi.entities.Menu;
import henrique.igor.restaurantmanagementapi.entities.dtos.menu.response.MenuResponseDTO;
import henrique.igor.restaurantmanagementapi.mapper.menu.MenuStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.menu.MenuJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListMenusUseCase {

    private final MenuJpaRepository menuRepository;
    private final MenuStructMapper mapper;

    public List<MenuResponseDTO> execute() {
        List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(mapper::toMenuResponseDTO)
                .toList();
    }
}