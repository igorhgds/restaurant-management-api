package henrique.igor.restaurantmanagementapi.usecases.dish;

import henrique.igor.restaurantmanagementapi.entities.Dish;
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
class DeleteDishByIdUseCaseTest {

    @Mock
    private DishJpaRepository dishRepository;

    @InjectMocks
    private DeleteDishByIdUseCase deleteDishByIdUseCase;

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
    @DisplayName("Should delete dish successfully")
    void shouldDeleteDishSuccessfully() {
        when(dishRepository.findById(dishId)).thenReturn(Optional.of(dish));

        deleteDishByIdUseCase.execute(dishId);

        verify(dishRepository).findById(dishId);
        verify(dishRepository).delete(dish);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when dish not found")
    void shouldThrowExceptionWhenDishNotFound() {
        when(dishRepository.findById(dishId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> deleteDishByIdUseCase.execute(dishId));

        verify(dishRepository).findById(dishId);
        verify(dishRepository, never()).delete(any(Dish.class));
    }

    @Test
    @DisplayName("Should delete disabled dish")
    void shouldDeleteDisabledDish() {
        dish.setEnabled(false);
        when(dishRepository.findById(dishId)).thenReturn(Optional.of(dish));

        deleteDishByIdUseCase.execute(dishId);

        verify(dishRepository).findById(dishId);
        verify(dishRepository).delete(dish);
    }

    @Test
    @DisplayName("Should delete dish with all categories")
    void shouldDeleteDishWithAllCategories() {
        for (Category category : Category.values()) {
            Dish categoryDish = new Dish();
            categoryDish.setDishId(UUID.randomUUID());
            categoryDish.setName("Dish " + category.name());
            categoryDish.setPrice(new BigDecimal("10.00"));
            categoryDish.setCategory(category);
            categoryDish.setEnabled(true);
            categoryDish.setDescription("Description");

            when(dishRepository.findById(categoryDish.getDishId())).thenReturn(Optional.of(categoryDish));

            deleteDishByIdUseCase.execute(categoryDish.getDishId());

            verify(dishRepository).findById(categoryDish.getDishId());
            verify(dishRepository).delete(categoryDish);
        }
    }
}