package henrique.igor.restaurantmanagementapi.usecases.dish;

import henrique.igor.restaurantmanagementapi.entities.Dish;
import henrique.igor.restaurantmanagementapi.entities.dtos.dish.response.DishResponseDTO;
import henrique.igor.restaurantmanagementapi.enums.Category;
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
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListDishesUseCaseTest {

    @Mock
    private DishJpaRepository dishRepository;

    @Mock
    private DishStructMapper dishMapper;

    @InjectMocks
    private ListDishesUseCase listDishesUseCase;

    private Dish dish1;
    private Dish dish2;
    private Dish dish3;

    @BeforeEach
    void setUp() {
        dish1 = new Dish();
        dish1.setDishId(UUID.randomUUID());
        dish1.setName("X-Burger");
        dish1.setPrice(new BigDecimal("29.90"));
        dish1.setCategory(Category.BURGERS);
        dish1.setEnabled(true);
        dish1.setDescription("Delicious burger");

        dish2 = new Dish();
        dish2.setDishId(UUID.randomUUID());
        dish2.setName("French Fries");
        dish2.setPrice(new BigDecimal("12.00"));
        dish2.setCategory(Category.SIDE_DISHES);
        dish2.setEnabled(true);
        dish2.setDescription("Crispy fries");

        dish3 = new Dish();
        dish3.setDishId(UUID.randomUUID());
        dish3.setName("Chocolate Cake");
        dish3.setPrice(new BigDecimal("18.00"));
        dish3.setCategory(Category.DESSERTS);
        dish3.setEnabled(false);
        dish3.setDescription("Rich chocolate cake");
    }

    @Test
    @DisplayName("Should return list of all dishes")
    void shouldReturnListOfAllDishes() {
        when(dishRepository.findAll()).thenReturn(List.of(dish1, dish2, dish3));

        DishResponseDTO dto1 = new DishResponseDTO(dish1.getDishId(), dish1.getName(), dish1.getPrice(), dish1.getDescription());
        DishResponseDTO dto2 = new DishResponseDTO(dish2.getDishId(), dish2.getName(), dish2.getPrice(), dish2.getDescription());
        DishResponseDTO dto3 = new DishResponseDTO(dish3.getDishId(), dish3.getName(), dish3.getPrice(), dish3.getDescription());
        when(dishMapper.toDishResponseDTO(dish1)).thenReturn(dto1);
        when(dishMapper.toDishResponseDTO(dish2)).thenReturn(dto2);
        when(dishMapper.toDishResponseDTO(dish3)).thenReturn(dto3);

        List<DishResponseDTO> result = listDishesUseCase.execute();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("X-Burger", result.get(0).name());
        assertEquals("French Fries", result.get(1).name());
        assertEquals("Chocolate Cake", result.get(2).name());

        verify(dishRepository).findAll();
        verify(dishMapper).toDishResponseDTO(dish1);
        verify(dishMapper).toDishResponseDTO(dish2);
        verify(dishMapper).toDishResponseDTO(dish3);
    }

    @Test
    @DisplayName("Should return empty list when no dishes")
    void shouldReturnEmptyListWhenNoDishes() {
        when(dishRepository.findAll()).thenReturn(List.of());

        List<DishResponseDTO> result = listDishesUseCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(dishRepository).findAll();
        verifyNoInteractions(dishMapper);
    }

    @Test
    @DisplayName("Should include disabled dishes in list")
    void shouldIncludeDisabledDishesInList() {
        when(dishRepository.findAll()).thenReturn(List.of(dish1, dish3));

        DishResponseDTO dto1 = new DishResponseDTO(dish1.getDishId(), dish1.getName(), dish1.getPrice(), dish1.getDescription());
        DishResponseDTO dto3 = new DishResponseDTO(dish3.getDishId(), dish3.getName(), dish3.getPrice(), dish3.getDescription());
        when(dishMapper.toDishResponseDTO(dish1)).thenReturn(dto1);
        when(dishMapper.toDishResponseDTO(dish3)).thenReturn(dto3);

        List<DishResponseDTO> result = listDishesUseCase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(d -> d.name().equals("X-Burger")));
        assertTrue(result.stream().anyMatch(d -> d.name().equals("Chocolate Cake")));
    }

    @Test
    @DisplayName("Should return dishes with all categories")
    void shouldReturnDishesWithAllCategories() {
        Dish mainDish = new Dish();
        mainDish.setDishId(UUID.randomUUID());
        mainDish.setName("Steak");
        mainDish.setPrice(new BigDecimal("50.00"));
        mainDish.setCategory(Category.MAIN_DISHES);
        mainDish.setEnabled(true);
        mainDish.setDescription("Grilled steak");

        Dish appetizer = new Dish();
        appetizer.setDishId(UUID.randomUUID());
        appetizer.setName("Salad");
        appetizer.setPrice(new BigDecimal("25.00"));
        appetizer.setCategory(Category.APPETIZERS);
        appetizer.setEnabled(true);
        appetizer.setDescription("Green salad");

        Dish drink = new Dish();
        drink.setDishId(UUID.randomUUID());
        drink.setName("Soda");
        drink.setPrice(new BigDecimal("8.00"));
        drink.setCategory(Category.DRINKS);
        drink.setEnabled(true);
        drink.setDescription("Cold soda");

        when(dishRepository.findAll()).thenReturn(List.of(mainDish, appetizer, drink));

        DishResponseDTO mainDto = new DishResponseDTO(mainDish.getDishId(), mainDish.getName(), mainDish.getPrice(), mainDish.getDescription());
        DishResponseDTO appDto = new DishResponseDTO(appetizer.getDishId(), appetizer.getName(), appetizer.getPrice(), appetizer.getDescription());
        DishResponseDTO drinkDto = new DishResponseDTO(drink.getDishId(), drink.getName(), drink.getPrice(), drink.getDescription());
        when(dishMapper.toDishResponseDTO(mainDish)).thenReturn(mainDto);
        when(dishMapper.toDishResponseDTO(appetizer)).thenReturn(appDto);
        when(dishMapper.toDishResponseDTO(drink)).thenReturn(drinkDto);

        List<DishResponseDTO> result = listDishesUseCase.execute();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(d -> d.name().equals("Steak")));
        assertTrue(result.stream().anyMatch(d -> d.name().equals("Salad")));
        assertTrue(result.stream().anyMatch(d -> d.name().equals("Soda")));
    }

    @Test
    @DisplayName("Should return dishes without description")
    void shouldReturnDishesWithoutDescription() {
        dish1.setDescription(null);

        when(dishRepository.findAll()).thenReturn(List.of(dish1));

        DishResponseDTO dto1 = new DishResponseDTO(dish1.getDishId(), dish1.getName(), dish1.getPrice(), null);
        when(dishMapper.toDishResponseDTO(dish1)).thenReturn(dto1);

        List<DishResponseDTO> result = listDishesUseCase.execute();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertNull(result.getFirst().description());
    }
}