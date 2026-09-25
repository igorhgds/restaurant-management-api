package henrique.igor.restaurantmanagementapi.usecases.menu;

import henrique.igor.restaurantmanagementapi.entities.Menu;
import henrique.igor.restaurantmanagementapi.entities.dtos.menu.request.FindMenusByFilterRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.menu.response.MenuResponseDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.pagination.PageableRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.pagination.PageableResponseDTO;
import henrique.igor.restaurantmanagementapi.mapper.menu.MenuStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.menu.MenuJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindMenusByFilterUseCaseTest {

    @Mock
    private MenuJpaRepository menuRepository;

    @Mock
    private MenuStructMapper mapper;

    @InjectMocks
    private FindMenusByFilterUseCase findMenusByFilterUseCase;

    private FindMenusByFilterRequestDTO filters;
    private Menu menu1;
    private Menu menu2;

    @BeforeEach
    void setUp() {
        filters = new FindMenusByFilterRequestDTO();
        setPageLimit(filters, 0, 10);

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

    private void setPageLimit(FindMenusByFilterRequestDTO dto, int page, int limit) {
        try {
            var pageField = PageableRequestDTO.class.getDeclaredField("page");
            var limitField = PageableRequestDTO.class.getDeclaredField("limit");
            pageField.setAccessible(true);
            limitField.setAccessible(true);
            pageField.set(dto, page);
            limitField.set(dto, limit);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Should return paginated menus with no filters")
    void shouldReturnPaginatedMenusWithNoFilters() {
        Page<Menu> pageOfMenus = new PageImpl<>(List.of(menu1, menu2), PageRequest.of(0, 10), 2);
        when(menuRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageOfMenus);

        MenuResponseDTO dto1 = new MenuResponseDTO(menu1);
        MenuResponseDTO dto2 = new MenuResponseDTO(menu2);

        when(mapper.toMenuResponseDTO(menu1)).thenReturn(dto1);
        when(mapper.toMenuResponseDTO(menu2)).thenReturn(dto2);

        PageableResponseDTO<MenuResponseDTO> result = findMenusByFilterUseCase.execute(filters);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("Winter Menu", result.getContent().get(0).name());
        assertEquals("Summer Menu", result.getContent().get(1).name());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(0, result.getPage());

        verify(menuRepository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    @DisplayName("Should filter by name")
    void shouldFilterByName() {
        filters.setName("Winter");

        Page<Menu> pageOfMenus = new PageImpl<>(List.of(menu1), PageRequest.of(0, 10), 1);
        when(menuRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageOfMenus);

        MenuResponseDTO dto1 = new MenuResponseDTO(menu1);
        when(mapper.toMenuResponseDTO(menu1)).thenReturn(dto1);

        PageableResponseDTO<MenuResponseDTO> result = findMenusByFilterUseCase.execute(filters);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Winter Menu", result.getContent().get(0).name());
    }

    @Test
    @DisplayName("Should return empty page when no menus found")
    void shouldReturnEmptyPageWhenNoMenusFound() {
        Page<Menu> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(menuRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(emptyPage);

        PageableResponseDTO<MenuResponseDTO> result = findMenusByFilterUseCase.execute(filters);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
    }
}
