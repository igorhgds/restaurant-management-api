package henrique.igor.restaurantmanagementapi.usecases.dish;

import henrique.igor.restaurantmanagementapi.entities.Dish;
import henrique.igor.restaurantmanagementapi.entities.dtos.dish.response.DishResponseDTO;
import henrique.igor.restaurantmanagementapi.enums.Category;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.mapper.dish.DishStructMapper;
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
class FindDishByIdUseCaseTest {

    @Mock
    private DishJpaRepository dishRepository;

    @Mock
    private DishStructMapper dishMapper;

    @InjectMocks
    private FindDishByIdUseCase findDishByIdUseCase;

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
    @DisplayName("Should throw EntityNotFoundException when dish not found")
    void shouldThrowExceptionWhenDishNotFound() {
        when(dishRepository.findById(dishId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> findDishByIdUseCase.execute(dishId));

        verify(dishRepository).findById(dishId);
        verifyNoInteractions(dishMapper);
    }

    @Test
    @DisplayName("Should return dish DTO when dish found")
    void shouldReturnDishWhenFound() {
        when(dishRepository.findById(dishId)).thenReturn(Optional.of(dish));

        DishResponseDTO responseDTO = new DishResponseDTO(
                dishId,
                "X-Burger",
                new BigDecimal("29.90"),
                "Delicious burger"
        );
        when(dishMapper.toDishResponseDTO(dish)).thenReturn(responseDTO);

        DishResponseDTO result = findDishByIdUseCase.execute(dishId);

        assertNotNull(result);
        assertEquals(dishId, result.dishId());
        assertEquals("X-Burger", result.name());
        assertEquals(new BigDecimal("29.90"), result.price());
        assertEquals("Delicious burger", result.description());

        verify(dishRepository).findById(dishId);
        verify(dishMapper).toDishResponseDTO(dish);
    }

    @Test
    @DisplayName("Should return dish with disabled status")
    void shouldReturnDishWithDisabledStatus() {
        dish.setEnabled(false);
        when(dishRepository.findById(dishId)).thenReturn(Optional.of(dish));

        DishResponseDTO responseDTO = new DishResponseDTO(
                dishId,
                "X-Burger",
                new BigDecimal("29.90"),
                "Delicious burger"
        );
        when(dishMapper.toDishResponseDTO(dish)).thenReturn(responseDTO);

        DishResponseDTO result = findDishByIdUseCase.execute(dishId);

        assertNotNull(result);
        assertEquals(dishId, result.dishId());
        assertEquals("X-Burger", result.name());
    }

    @Test
    @DisplayName("Should return dish without description")
    void shouldReturnDishWithoutDescription() {
        dish.setDescription(null);
        when(dishRepository.findById(dishId)).thenReturn(Optional.of(dish));

        DishResponseDTO responseDTO = new DishResponseDTO(
                dishId,
                "X-Burger",
                new BigDecimal("29.90"),
                null
        );
        when(dishMapper.toDishResponseDTO(dish)).thenReturn(responseDTO);

        DishResponseDTO result = findDishByIdUseCase.execute(dishId);

        assertNotNull(result);
        assertNull(result.description());
    }
}