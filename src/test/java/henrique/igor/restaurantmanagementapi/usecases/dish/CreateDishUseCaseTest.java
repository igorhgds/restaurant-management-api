package henrique.igor.restaurantmanagementapi.usecases.dish;

import henrique.igor.restaurantmanagementapi.entities.Dish;
import henrique.igor.restaurantmanagementapi.entities.dtos.dish.request.CreateDishRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.dish.response.DishResponseDTO;
import henrique.igor.restaurantmanagementapi.enums.Category;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.repositories.dish.DishJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateDishUseCaseTest {

    @Mock
    private DishJpaRepository dishRepository;

    @InjectMocks
    private CreateDishUseCase createDishUseCase;

    private CreateDishRequestDTO request;

    @BeforeEach
    void setUp() {
        request = new CreateDishRequestDTO("X-Burger", new BigDecimal("29.90"), Category.BURGERS, true, "Delicious burger");
    }

    @Test
    @DisplayName("Should create dish successfully")
    void shouldCreateDishSuccessfully() {
        when(dishRepository.existsByName("X-Burger")).thenReturn(false);

        Dish savedDish = new Dish();
        savedDish.setDishId(UUID.randomUUID());
        savedDish.setName(request.name());
        savedDish.setPrice(request.price());
        savedDish.setCategory(request.category());
        savedDish.setEnabled(request.isEnabled());
        savedDish.setDescription(request.description());

        when(dishRepository.save(any(Dish.class))).thenReturn(savedDish);

        DishResponseDTO result = createDishUseCase.execute(request);

        assertNotNull(result);
        assertEquals("X-Burger", result.name());
        assertEquals(new BigDecimal("29.90"), result.price());
        assertEquals("Delicious burger", result.description());
        assertEquals(savedDish.getDishId(), result.dishId());

        verify(dishRepository).existsByName("X-Burger");
        verify(dishRepository).save(any(Dish.class));
    }

    @Test
    @DisplayName("Should throw BusinessRuleException when dish name already exists")
    void shouldThrowExceptionWhenDishNameDuplicated() {
        when(dishRepository.existsByName("X-Burger")).thenReturn(true);

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> createDishUseCase.execute(request)
        );

        assertEquals(ExceptionCode.DUPLICATED_RESOURCE, exception.getCode());
        verify(dishRepository).existsByName("X-Burger");
        verify(dishRepository, never()).save(any(Dish.class));
    }

    @Test
    @DisplayName("Should create dish with all categories")
    void shouldCreateDishWithAllCategories() {
        for (Category category : Category.values()) {
            CreateDishRequestDTO categoryRequest = new CreateDishRequestDTO(
                    "Dish " + category.name(),
                    new BigDecimal("10.00"),
                    category,
                    true,
                    "Description"
            );

            when(dishRepository.existsByName(categoryRequest.name())).thenReturn(false);

            Dish savedDish = new Dish();
            savedDish.setDishId(UUID.randomUUID());
            savedDish.setName(categoryRequest.name());
            savedDish.setPrice(categoryRequest.price());
            savedDish.setCategory(categoryRequest.category());
            savedDish.setEnabled(categoryRequest.isEnabled());
            savedDish.setDescription(categoryRequest.description());

            when(dishRepository.save(any(Dish.class))).thenReturn(savedDish);

            DishResponseDTO result = createDishUseCase.execute(categoryRequest);

            assertNotNull(result);
            assertEquals(categoryRequest.name(), result.name());
        }
    }

    @Test
    @DisplayName("Should create dish with disabled status")
    void shouldCreateDishWithDisabledStatus() {
        CreateDishRequestDTO disabledRequest = new CreateDishRequestDTO(
                "Disabled Dish",
                new BigDecimal("15.00"),
                Category.MAIN_DISHES,
                false,
                "Disabled description"
        );

        when(dishRepository.existsByName("Disabled Dish")).thenReturn(false);

        Dish savedDish = new Dish();
        savedDish.setDishId(UUID.randomUUID());
        savedDish.setName(disabledRequest.name());
        savedDish.setPrice(disabledRequest.price());
        savedDish.setCategory(disabledRequest.category());
        savedDish.setEnabled(false);
        savedDish.setDescription(disabledRequest.description());

        when(dishRepository.save(any(Dish.class))).thenReturn(savedDish);

        DishResponseDTO result = createDishUseCase.execute(disabledRequest);

        assertNotNull(result);
        assertEquals("Disabled Dish", result.name());
    }

    @Test
    @DisplayName("Should create dish without description")
    void shouldCreateDishWithoutDescription() {
        CreateDishRequestDTO noDescRequest = new CreateDishRequestDTO(
                "No Description Dish",
                new BigDecimal("12.50"),
                Category.DRINKS,
                true,
                null
        );

        when(dishRepository.existsByName("No Description Dish")).thenReturn(false);

        Dish savedDish = new Dish();
        savedDish.setDishId(UUID.randomUUID());
        savedDish.setName(noDescRequest.name());
        savedDish.setPrice(noDescRequest.price());
        savedDish.setCategory(noDescRequest.category());
        savedDish.setEnabled(true);
        savedDish.setDescription(null);

        when(dishRepository.save(any(Dish.class))).thenReturn(savedDish);

        DishResponseDTO result = createDishUseCase.execute(noDescRequest);

        assertNotNull(result);
        assertEquals("No Description Dish", result.name());
        assertNull(result.description());
    }
}