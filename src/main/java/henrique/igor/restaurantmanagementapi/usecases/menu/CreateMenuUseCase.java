package henrique.igor.restaurantmanagementapi.usecases.menu;

import henrique.igor.restaurantmanagementapi.entities.Menu;
import henrique.igor.restaurantmanagementapi.entities.dtos.menu.request.CreateMenuRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.menu.response.MenuResponseDTO;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.repositories.menu.MenuJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateMenuUseCase {

    private final MenuJpaRepository menuRepository;

    public MenuResponseDTO execute(CreateMenuRequestDTO request) {
        if (menuRepository.existsByName(request.name())) {
            throw new BusinessRuleException(ExceptionCode.DUPLICATED_RESOURCE);
        }

        Menu menu = new Menu();
        menu.setName(request.name());
        menu.setDescription(request.description());
        menu.setActive(request.active());
        menu.setStartDate(request.startDate());
        menu.setEndDate(request.endDate());

        Menu saved = menuRepository.save(menu);
        return new MenuResponseDTO(saved);
    }
}