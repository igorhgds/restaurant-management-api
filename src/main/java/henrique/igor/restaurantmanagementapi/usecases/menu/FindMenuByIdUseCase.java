package henrique.igor.restaurantmanagementapi.usecases.menu;

import henrique.igor.restaurantmanagementapi.entities.Menu;
import henrique.igor.restaurantmanagementapi.entities.dtos.menu.response.MenuResponseDTO;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.mapper.menu.MenuStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.menu.MenuJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindMenuByIdUseCase {

    private final MenuJpaRepository menuRepository;
    private final MenuStructMapper mapper;

    public MenuResponseDTO execute(UUID menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new EntityNotFoundException(Menu.class));

        return mapper.toMenuResponseDTO(menu);
    }
}