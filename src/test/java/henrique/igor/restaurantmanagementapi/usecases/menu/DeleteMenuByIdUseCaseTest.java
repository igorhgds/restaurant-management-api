package henrique.igor.restaurantmanagementapi.usecases.menu;

import henrique.igor.restaurantmanagementapi.entities.Menu;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.repositories.menu.MenuDishJpaRepository;
import henrique.igor.restaurantmanagementapi.repositories.menu.MenuJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteMenuByIdUseCaseTest {

    @Mock
    private MenuJpaRepository menuRepository;

    @Mock
    private MenuDishJpaRepository menuDishRepository;

    @InjectMocks
    private DeleteMenuByIdUseCase deleteMenuByIdUseCase;

    private UUID menuId;
    private Menu menu;

    @BeforeEach
    void setUp() {
        menuId = UUID.randomUUID();
        menu = new Menu();
        menu.setMenuId(menuId);
        menu.setName("Winter Menu");
        menu.setDescription("Special winter dishes");
        menu.setActive(true);
        menu.setStartDate(LocalDateTime.of(2025, 1, 1, 0, 0));
        menu.setEndDate(LocalDateTime.of(2025, 3, 31, 23, 59));
        menu.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should delete menu successfully")
    void shouldDeleteMenuSuccessfully() {
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));

        deleteMenuByIdUseCase.execute(menuId);

        verify(menuRepository).findById(menuId);
        verify(menuDishRepository).deleteByMenuId(menuId);
        verify(menuRepository).delete(menu);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when menu not found")
    void shouldThrowExceptionWhenMenuNotFound() {
        when(menuRepository.findById(menuId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> deleteMenuByIdUseCase.execute(menuId));

        verify(menuRepository).findById(menuId);
        verify(menuDishRepository, never()).deleteByMenuId(any(UUID.class));
        verify(menuRepository, never()).delete(any(Menu.class));
    }
}
