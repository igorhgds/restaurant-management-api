package henrique.igor.restaurantmanagementapi.usecases.menu;

import henrique.igor.restaurantmanagementapi.entities.Menu;
import henrique.igor.restaurantmanagementapi.entities.dtos.menu.request.UpdateMenuRequestDTO;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
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
class UpdateMenuUseCaseTest {

    @Mock
    private MenuJpaRepository menuRepository;

    @InjectMocks
    private UpdateMenuUseCase updateMenuUseCase;

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
    @DisplayName("Should update menu fields")
    void shouldUpdateMenuFields() {
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));

        LocalDateTime newStart = LocalDateTime.of(2025, 2, 1, 0, 0);
        LocalDateTime newEnd = LocalDateTime.of(2025, 2, 28, 23, 59);

        UpdateMenuRequestDTO request = new UpdateMenuRequestDTO(
                "Updated Winter Menu",
                "Updated description",
                false,
                newStart,
                newEnd
        );

        updateMenuUseCase.execute(request, menuId);

        assertEquals("Updated Winter Menu", menu.getName());
        assertEquals("Updated description", menu.getDescription());
        assertFalse(menu.isActive());
        assertEquals(newStart, menu.getStartDate());
        assertEquals(newEnd, menu.getEndDate());
        verify(menuRepository).findById(menuId);
    }

    @Test
    @DisplayName("Should not update fields when null is provided")
    void shouldNotUpdateFieldsWhenNullProvided() {
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));

        String originalName = menu.getName();
        String originalDescription = menu.getDescription();
        boolean originalActive = menu.isActive();
        LocalDateTime originalStart = menu.getStartDate();
        LocalDateTime originalEnd = menu.getEndDate();

        UpdateMenuRequestDTO request = new UpdateMenuRequestDTO(null, null, null, null, null);

        updateMenuUseCase.execute(request, menuId);

        assertEquals(originalName, menu.getName());
        assertEquals(originalDescription, menu.getDescription());
        assertEquals(originalActive, menu.isActive());
        assertEquals(originalStart, menu.getStartDate());
        assertEquals(originalEnd, menu.getEndDate());
        verify(menuRepository).findById(menuId);
    }

    @Test
    @DisplayName("Should not update name when blank provided")
    void shouldNotUpdateNameWhenBlank() {
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));
        String originalName = menu.getName();

        UpdateMenuRequestDTO request = new UpdateMenuRequestDTO("   ", null, null, null, null);
        updateMenuUseCase.execute(request, menuId);

        assertEquals(originalName, menu.getName());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when menu not found")
    void shouldThrowExceptionWhenMenuNotFound() {
        when(menuRepository.findById(menuId)).thenReturn(Optional.empty());

        UpdateMenuRequestDTO request = new UpdateMenuRequestDTO("New Name", null, null, null, null);

        assertThrows(EntityNotFoundException.class, () -> updateMenuUseCase.execute(request, menuId));

        verify(menuRepository).findById(menuId);
    }
}
