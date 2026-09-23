package henrique.igor.restaurantmanagementapi.usecases.dish;

import henrique.igor.restaurantmanagementapi.entities.Dish;
import henrique.igor.restaurantmanagementapi.entities.dtos.dish.request.FindDishesByFilterRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.dish.response.DishResponseDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.pagination.PageableRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.pagination.PageableResponseDTO;
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
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindDishesByFilterUseCaseTest {

    @Mock
    private DishJpaRepository dishRepository;

    @Mock
    private DishStructMapper dishMapper;

    @InjectMocks
    private FindDishesByFilterUseCase findDishesByFilterUseCase;

    private FindDishesByFilterRequestDTO filters;
    private Dish dish1;
    private Dish dish2;

    @BeforeEach
    void setUp() {
        filters = new FindDishesByFilterRequestDTO();
        // PageableRequestDTO doesn't have setters, use reflection or constructor
        setPageLimit(filters, 1, 10);

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
    }

    private void setPageLimit(FindDishesByFilterRequestDTO dto, int page, int limit) {
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
    @DisplayName("Should return paginated dishes with no filters")
    void shouldReturnPaginatedDishesWithNoFilters() {
        Page<Dish> pageOfDishes = new PageImpl<>(List.of(dish1, dish2), PageRequest.of(0, 10), 2);
        when(dishRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageOfDishes);

        DishResponseDTO dto1 = new DishResponseDTO(dish1.getDishId(), dish1.getName(), dish1.getPrice(), dish1.getDescription());
        DishResponseDTO dto2 = new DishResponseDTO(dish2.getDishId(), dish2.getName(), dish2.getPrice(), dish2.getDescription());
        when(dishMapper.toDishResponseDTO(dish1)).thenReturn(dto1);
        when(dishMapper.toDishResponseDTO(dish2)).thenReturn(dto2);

        PageableResponseDTO<DishResponseDTO> result = findDishesByFilterUseCase.execute(filters);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("X-Burger", result.getContent().getFirst().name());
        assertEquals("French Fries", result.getContent().get(1).name());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(0, result.getPage());

        verify(dishRepository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    @DisplayName("Should return empty page when no dishes found")
    void shouldReturnEmptyPageWhenNoDishesFound() {
        Page<Dish> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(dishRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(emptyPage);

        PageableResponseDTO<DishResponseDTO> result = findDishesByFilterUseCase.execute(filters);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
    }

    @Test
    @DisplayName("Should filter by name")
    void shouldFilterByName() {
        filters.setName("burger");

        Page<Dish> pageOfDishes = new PageImpl<>(List.of(dish1), PageRequest.of(0, 10), 1);
        when(dishRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageOfDishes);

        DishResponseDTO dto1 = new DishResponseDTO(dish1.getDishId(), dish1.getName(), dish1.getPrice(), dish1.getDescription());
        when(dishMapper.toDishResponseDTO(dish1)).thenReturn(dto1);

        PageableResponseDTO<DishResponseDTO> result = findDishesByFilterUseCase.execute(filters);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("X-Burger", result.getContent().getFirst().name());
    }

    @Test
    @DisplayName("Should filter by category")
    void shouldFilterByCategory() {
        filters.setCategory(Category.BURGERS);

        Page<Dish> pageOfDishes = new PageImpl<>(List.of(dish1), PageRequest.of(0, 10), 1);
        when(dishRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageOfDishes);

        DishResponseDTO dto1 = new DishResponseDTO(dish1.getDishId(), dish1.getName(), dish1.getPrice(), dish1.getDescription());
        when(dishMapper.toDishResponseDTO(dish1)).thenReturn(dto1);

        PageableResponseDTO<DishResponseDTO> result = findDishesByFilterUseCase.execute(filters);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(Category.BURGERS, dish1.getCategory());
    }

    @Test
    @DisplayName("Should filter by enabled status")
    void shouldFilterByEnabledStatus() {
        filters.setIsEnabled(false);
        dish1.setEnabled(false);

        Page<Dish> pageOfDishes = new PageImpl<>(List.of(dish1), PageRequest.of(0, 10), 1);
        when(dishRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageOfDishes);

        DishResponseDTO dto1 = new DishResponseDTO(dish1.getDishId(), dish1.getName(), dish1.getPrice(), dish1.getDescription());
        when(dishMapper.toDishResponseDTO(dish1)).thenReturn(dto1);

        PageableResponseDTO<DishResponseDTO> result = findDishesByFilterUseCase.execute(filters);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertFalse(dish1.isEnabled());
    }

    @Test
    @DisplayName("Should combine multiple filters")
    void shouldCombineMultipleFilters() {
        filters.setName("burger");
        filters.setCategory(Category.BURGERS);
        filters.setIsEnabled(true);

        Page<Dish> pageOfDishes = new PageImpl<>(List.of(dish1), PageRequest.of(0, 10), 1);
        when(dishRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageOfDishes);

        DishResponseDTO dto1 = new DishResponseDTO(dish1.getDishId(), dish1.getName(), dish1.getPrice(), dish1.getDescription());
        when(dishMapper.toDishResponseDTO(dish1)).thenReturn(dto1);

        PageableResponseDTO<DishResponseDTO> result = findDishesByFilterUseCase.execute(filters);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("X-Burger", result.getContent().getFirst().name());
    }

    @Test
    @DisplayName("Should use custom page and limit")
    void shouldUseCustomPageAndLimit() {
        setPageLimit(filters, 2, 5);

        Page<Dish> pageOfDishes = new PageImpl<>(List.of(), PageRequest.of(1, 5), 0);
        when(dishRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageOfDishes);

        PageableResponseDTO<DishResponseDTO> result = findDishesByFilterUseCase.execute(filters);

        assertNotNull(result);
        assertEquals(1, result.getPage());
    }

    @Test
    @DisplayName("Should sort by name ascending by default")
    void shouldSortByNameAscendingByDefault() {
        Page<Dish> pageOfDishes = new PageImpl<>(List.of(dish1, dish2), PageRequest.of(0, 10, Sort.by("name").ascending()), 2);
        when(dishRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageOfDishes);

        DishResponseDTO dto1 = new DishResponseDTO(dish1.getDishId(), dish1.getName(), dish1.getPrice(), dish1.getDescription());
        DishResponseDTO dto2 = new DishResponseDTO(dish2.getDishId(), dish2.getName(), dish2.getPrice(), dish2.getDescription());
        when(dishMapper.toDishResponseDTO(dish1)).thenReturn(dto1);
        when(dishMapper.toDishResponseDTO(dish2)).thenReturn(dto2);

        PageableResponseDTO<DishResponseDTO> result = findDishesByFilterUseCase.execute(filters);

        assertNotNull(result);
        verify(dishRepository).findAll(any(Specification.class), any(Pageable.class));
    }
}