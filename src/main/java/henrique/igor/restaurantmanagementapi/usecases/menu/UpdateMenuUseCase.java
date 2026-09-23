package henrique.igor.restaurantmanagementapi.usecases.menu;

import henrique.igor.restaurantmanagementapi.entities.Menu;
import henrique.igor.restaurantmanagementapi.entities.dtos.menu.request.UpdateMenuRequestDTO;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.repositories.menu.MenuJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateMenuUseCase {

    private final MenuJpaRepository menuRepository;

    @Transactional
    public void execute(UpdateMenuRequestDTO request, UUID menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new EntityNotFoundException(Menu.class));

        menu.updateInfo(
                request.name(),
                request.description(),
                request.active(),
                request.startDate(),
                request.endDate()
        );
    }
}