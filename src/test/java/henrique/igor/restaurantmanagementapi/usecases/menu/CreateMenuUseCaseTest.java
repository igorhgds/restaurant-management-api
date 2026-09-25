package henrique.igor.restaurantmanagementapi.usecases.menu;

import henrique.igor.restaurantmanagementapi.entities.Menu;
import henrique.igor.restaurantmanagementapi.entities.dtos.menu.request.CreateMenuRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.menu.response.MenuResponseDTO;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.repositories.menu.MenuJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateMenuUseCaseTest {

    @Mock
    private MenuJpaRepository menuRepository;

    @InjectMocks
    private CreateMenuUseCase createMenuUseCase;

    private CreateMenuRequestDTO request;

    @BeforeEach
    void setUp() {
        request = new CreateMenuRequestDTO(
                "Winter Menu",
                "Special winter dishes",
                true,
                LocalDateTime.of(2025, 1, 1, 0, 0),
                LocalDateTime.of(2025, 3, 31, 23, 59)
        );
    }

    @Test
    @DisplayName("Should create menu successfully")
    void shouldCreateMenuSuccessfully() {
        when(menuRepository.existsByName(request.name())).thenReturn(false);

        Menu savedMenu = new Menu();
        savedMenu.setMenuId(UUID.randomUUID());
        savedMenu.setName(request.name());
        savedMenu.setDescription(request.description());
        savedMenu.setActive(request.active());
        savedMenu.setStartDate(request.startDate());
        savedMenu.setEndDate(request.endDate());
        savedMenu.setCreatedAt(LocalDateTime.now());

        when(menuRepository.save(any(Menu.class))).thenReturn(savedMenu);

        MenuResponseDTO result = createMenuUseCase.execute(request);

        assertNotNull(result);
        assertEquals(request.name(), result.name());
        assertEquals(request.description(), result.description());
        assertEquals(request.active(), result.active());
        assertEquals(request.startDate(), result.startDate());
        assertEquals(request.endDate(), result.endDate());
        assertNotNull(result.menuId());

        verify(menuRepository).existsByName(request.name());
        verify(menuRepository).save(any(Menu.class));
    }

    @Test
    @DisplayName("Should throw BusinessRuleException when menu name is duplicated")
    void shouldThrowExceptionWhenMenuNameDuplicated() {
        when(menuRepository.existsByName(request.name())).thenReturn(true);

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> createMenuUseCase.execute(request)
        );

        assertEquals(ExceptionCode.DUPLICATED_RESOURCE, exception.getCode());
        verify(menuRepository).existsByName(request.name());
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    @DisplayName("Should create menu with null description, startDate, and endDate")
    void shouldCreateMenuWithNullOptionalFields() {
        CreateMenuRequestDTO partialRequest = new CreateMenuRequestDTO(
                "Basic Menu",
                null,
                false,
                null,
                null
        );

        when(menuRepository.existsByName(partialRequest.name())).thenReturn(false);

        Menu savedMenu = new Menu();
        savedMenu.setMenuId(UUID.randomUUID());
        savedMenu.setName(partialRequest.name());
        savedMenu.setDescription(null);
        savedMenu.setActive(partialRequest.active());
        savedMenu.setStartDate(null);
        savedMenu.setEndDate(null);

        when(menuRepository.save(any(Menu.class))).thenReturn(savedMenu);

        MenuResponseDTO result = createMenuUseCase.execute(partialRequest);

        assertNotNull(result);
        assertEquals(partialRequest.name(), result.name());
        assertNull(result.description());
        assertEquals(partialRequest.active(), result.active());
        assertNull(result.startDate());
        assertNull(result.endDate());
    }
}
