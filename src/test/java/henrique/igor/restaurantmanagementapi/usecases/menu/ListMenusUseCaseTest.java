package henrique.igor.restaurantmanagementapi.usecases.menu;

import henrique.igor.restaurantmanagementapi.entities.Menu;
import henrique.igor.restaurantmanagementapi.entities.dtos.menu.response.MenuResponseDTO;
import henrique.igor.restaurantmanagementapi.mapper.menu.MenuStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.menu.MenuJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListMenusUseCaseTest {

    @Mock
    private MenuJpaRepository menuRepository;

    @Mock
    private MenuStructMapper mapper;

    @InjectMocks
    private ListMenusUseCase listMenusUseCase;

    private Menu menu1;
    private Menu menu2;

    @BeforeEach
    void setUp() {
        menu1 = new Menu();
        menu1.setMenuId(UUID.randomUUID());
        menu1.setName("Winter Menu");
        menu1.setDescription("Special winter dishes");
        menu1.setActive(true);
        menu1.setStartDate(LocalDateTime.of(2025, 1, 1, 0, 0));
        menu1.setEndDate(LocalDateTime.of(2025, 3, 31, 23, 59));

        menu2 = new Menu();
        menu2.setMenuId(UUID.randomUUID());
        menu2.setName("Summer Menu");
        menu2.setDescription("Fresh summer dishes");
        menu2.setActive(false);
        menu2.setStartDate(LocalDateTime.of(2025, 6, 1, 0, 0));
        menu2.setEndDate(LocalDateTime.of(2025, 8, 31, 23, 59));
    }

    @Test
    @DisplayName("Should return list of all menus")
    void shouldReturnListOfAllMenus() {
        when(menuRepository.findAll()).thenReturn(List.of(menu1, menu2));

        MenuResponseDTO dto1 = new MenuResponseDTO(menu1);
        MenuResponseDTO dto2 = new MenuResponseDTO(menu2);

        when(mapper.toMenuResponseDTO(menu1)).thenReturn(dto1);
        when(mapper.toMenuResponseDTO(menu2)).thenReturn(dto2);

        List<MenuResponseDTO> result = listMenusUseCase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Winter Menu", result.get(0).name());
        assertEquals("Summer Menu", result.get(1).name());

        verify(menuRepository).findAll();
        verify(mapper).toMenuResponseDTO(menu1);
        verify(mapper).toMenuResponseDTO(menu2);
    }

    @Test
    @DisplayName("Should return empty list when no menus")
    void shouldReturnEmptyListWhenNoMenus() {
        when(menuRepository.findAll()).thenReturn(List.of());

        List<MenuResponseDTO> result = listMenusUseCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(menuRepository).findAll();
        verifyNoInteractions(mapper);
    }
}
