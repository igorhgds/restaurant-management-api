package henrique.igor.restaurantmanagementapi.usecases.menu;

import henrique.igor.restaurantmanagementapi.entities.Menu;
import henrique.igor.restaurantmanagementapi.entities.dtos.menu.response.MenuResponseDTO;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindMenuByIdUseCaseTest {

    @Mock
    private MenuJpaRepository menuRepository;

    @Mock
    private MenuStructMapper mapper;

    @InjectMocks
    private FindMenuByIdUseCase findMenuByIdUseCase;

    private UUID menuId;
    private Menu menu;
    private MenuResponseDTO responseDTO;

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
        menu.setUpdatedAt(null);

        responseDTO = new MenuResponseDTO(
                menuId,
                "Winter Menu",
                "Special winter dishes",
                true,
                LocalDateTime.of(2025, 1, 1, 0, 0),
                LocalDateTime.of(2025, 3, 31, 23, 59),
                menu.getCreatedAt(),
                null
        );
    }

    @Test
    @DisplayName("Should return menu DTO when menu is found")
    void shouldReturnMenuWhenFound() {
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));
        when(mapper.toMenuResponseDTO(menu)).thenReturn(responseDTO);

        MenuResponseDTO result = findMenuByIdUseCase.execute(menuId);

        assertNotNull(result);
        assertEquals(menuId, result.menuId());
        assertEquals("Winter Menu", result.name());
        assertEquals("Special winter dishes", result.description());
        assertEquals(true, result.active());
        assertEquals(LocalDateTime.of(2025, 1, 1, 0, 0), result.startDate());
        assertEquals(LocalDateTime.of(2025, 3, 31, 23, 59), result.endDate());

        verify(menuRepository).findById(menuId);
        verify(mapper).toMenuResponseDTO(menu);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when menu not found")
    void shouldThrowExceptionWhenMenuNotFound() {
        when(menuRepository.findById(menuId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> findMenuByIdUseCase.execute(menuId));

        verify(menuRepository).findById(menuId);
        verifyNoInteractions(mapper);
    }
}
