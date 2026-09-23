package henrique.igor.restaurantmanagementapi.usecases.dish;

import henrique.igor.restaurantmanagementapi.entities.Dish;
import henrique.igor.restaurantmanagementapi.entities.dtos.dish.request.UpdateDishRequestDTO;
import henrique.igor.restaurantmanagementapi.enums.Category;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.repositories.dish.DishJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateDishUseCaseTest {

    @Mock
    private DishJpaRepository dishRepository;

    @InjectMocks
    private UpdateDishUseCase updateDishUseCase;

    private UUID dishId;
    private Dish dish;

    @BeforeEach
    void setUp() {
        dishId = UUID.randomUUID();
        dish = new Dish();
        dish.setDishId(dishId);
        dish.setName("X-Burger");
        dish.setPrice(new BigDecimal("29.90"));
        dish.setCategory(Category.BURGERS);
        dish.setEnabled(true);
        dish.setDescription("Delicious burger");
    }

    @Test
    @DisplayName("Should update dish name")
    void shouldUpdateDishName() {
        when(dishRepository.findById(dishId)).thenReturn(Optional.of(dish));

        UpdateDishRequestDTO request = new UpdateDishRequestDTO("X-Burger Premium", null, null, null, null);

        updateDishUseCase.execute(request, dishId);

        assertEquals("X-Burger Premium", dish.getName());
        verify(dishRepository).findById(dishId);
    }

    @Test
    @DisplayName("Should update dish price")
    void shouldUpdateDishPrice() {
        when(dishRepository.findById(dishId)).thenReturn(Optional.of(dish));

        UpdateDishRequestDTO request = new UpdateDishRequestDTO(null, new BigDecimal("35.00"), null, null, null);

        updateDishUseCase.execute(request, dishId);

        assertEquals(new BigDecimal("35.00"), dish.getPrice());
        verify(dishRepository).findById(dishId);
    }

    @Test
    @DisplayName("Should update dish category")
    void shouldUpdateDishCategory() {
        when(dishRepository.findById(dishId)).thenReturn(Optional.of(dish));

        UpdateDishRequestDTO request = new UpdateDishRequestDTO(null, null, Category.MAIN_DISHES, null, null);

        updateDishUseCase.execute(request, dishId);

        assertEquals(Category.MAIN_DISHES, dish.getCategory());
        verify(dishRepository).findById(dishId);
    }

    @Test
    @DisplayName("Should update dish enabled status")
    void shouldUpdateDishEnabledStatus() {
        when(dishRepository.findById(dishId)).thenReturn(Optional.of(dish));

        UpdateDishRequestDTO request = new UpdateDishRequestDTO(null, null, null, false, null);

        updateDishUseCase.execute(request, dishId);

        assertFalse(dish.isEnabled());
        verify(dishRepository).findById(dishId);
    }

    @Test
    @DisplayName("Should update dish description")
    void shouldUpdateDishDescription() {
        when(dishRepository.findById(dishId)).thenReturn(Optional.of(dish));

        UpdateDishRequestDTO request = new UpdateDishRequestDTO(null, null, null, null, "New description");

        updateDishUseCase.execute(request, dishId);

        assertEquals("New description", dish.getDescription());
        verify(dishRepository).findById(dishId);
    }

    @Test
    @DisplayName("Should update multiple fields at once")
    void shouldUpdateMultipleFields() {
        when(dishRepository.findById(dishId)).thenReturn(Optional.of(dish));

        UpdateDishRequestDTO request = new UpdateDishRequestDTO(
                "Updated Burger",
                new BigDecimal("39.90"),
                Category.BURGERS,
                false,
                "Updated description"
        );

        updateDishUseCase.execute(request, dishId);

        assertEquals("Updated Burger", dish.getName());
        assertEquals(new BigDecimal("39.90"), dish.getPrice());
        assertEquals(Category.BURGERS, dish.getCategory());
        assertFalse(dish.isEnabled());
        assertEquals("Updated description", dish.getDescription());
        verify(dishRepository).findById(dishId);
    }

    @Test
    @DisplayName("Should not update fields when null is provided")
    void shouldNotUpdateFieldsWhenNullProvided() {
        when(dishRepository.findById(dishId)).thenReturn(Optional.of(dish));

        String originalName = dish.getName();
        BigDecimal originalPrice = dish.getPrice();
        Category originalCategory = dish.getCategory();
        boolean originalEnabled = dish.isEnabled();
        String originalDescription = dish.getDescription();

        UpdateDishRequestDTO request = new UpdateDishRequestDTO(null, null, null, null, null);

        updateDishUseCase.execute(request, dishId);

        assertEquals(originalName, dish.getName());
        assertEquals(originalPrice, dish.getPrice());
        assertEquals(originalCategory, dish.getCategory());
        assertEquals(originalEnabled, dish.isEnabled());
        assertEquals(originalDescription, dish.getDescription());
        verify(dishRepository).findById(dishId);
    }

    @Test
    @DisplayName("Should not update price when zero or negative provided")
    void shouldNotUpdatePriceWhenZeroOrNegative() {
        when(dishRepository.findById(dishId)).thenReturn(Optional.of(dish));
        BigDecimal originalPrice = dish.getPrice();

        UpdateDishRequestDTO requestZero = new UpdateDishRequestDTO(null, BigDecimal.ZERO, null, null, null);
        updateDishUseCase.execute(requestZero, dishId);
        assertEquals(originalPrice, dish.getPrice());

        UpdateDishRequestDTO requestNegative = new UpdateDishRequestDTO(null, new BigDecimal("-10.00"), null, null, null);
        updateDishUseCase.execute(requestNegative, dishId);
        assertEquals(originalPrice, dish.getPrice());
    }

    @Test
    @DisplayName("Should not update name when blank provided")
    void shouldNotUpdateNameWhenBlank() {
        when(dishRepository.findById(dishId)).thenReturn(Optional.of(dish));
        String originalName = dish.getName();

        UpdateDishRequestDTO request = new UpdateDishRequestDTO("   ", null, null, null, null);
        updateDishUseCase.execute(request, dishId);

        assertEquals(originalName, dish.getName());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when dish not found")
    void shouldThrowExceptionWhenDishNotFound() {
        when(dishRepository.findById(dishId)).thenReturn(Optional.empty());

        UpdateDishRequestDTO request = new UpdateDishRequestDTO("New Name", null, null, null, null);

        assertThrows(EntityNotFoundException.class, () -> updateDishUseCase.execute(request, dishId));

        verify(dishRepository).findById(dishId);
    }
}