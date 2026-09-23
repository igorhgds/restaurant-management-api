package henrique.igor.restaurantmanagementapi.usecases.menu;

import henrique.igor.restaurantmanagementapi.entities.Menu;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.repositories.menu.MenuDishJpaRepository;
import henrique.igor.restaurantmanagementapi.repositories.menu.MenuJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteMenuByIdUseCase {

    private final MenuJpaRepository menuRepository;
    private final MenuDishJpaRepository menuDishRepository;

    public void execute(UUID menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new EntityNotFoundException(Menu.class));

        // Remove associations first
        menuDishRepository.deleteByMenuId(menuId);

        menuRepository.delete(menu);
    }
}