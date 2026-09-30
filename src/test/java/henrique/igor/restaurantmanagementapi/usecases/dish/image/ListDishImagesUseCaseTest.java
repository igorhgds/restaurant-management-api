package henrique.igor.restaurantmanagementapi.usecases.dish.image;

import henrique.igor.restaurantmanagementapi.entities.DishImage;
import henrique.igor.restaurantmanagementapi.entities.Image;
import henrique.igor.restaurantmanagementapi.entities.dtos.image.ImageResponseDTO;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.mapper.image.ImageStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.dish.DishJpaRepository;
import henrique.igor.restaurantmanagementapi.repositories.image.DishImageJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListDishImagesUseCaseTest {

    @Mock
    private DishJpaRepository dishJpaRepository;
    @Mock
    private DishImageJpaRepository dishImageJpaRepository;
    @Mock
    private ImageStructMapper imageStructMapper;

    @InjectMocks
    private ListDishImagesUseCase listDishImagesUseCase;

    private UUID dishId;

    @BeforeEach
    void setUp() {
        dishId = UUID.randomUUID();
    }

    @Test
    void shouldListDishImagesSuccessfully() {
        when(dishJpaRepository.existsById(dishId)).thenReturn(true);

        Image image = new Image();
        image.setImageId(UUID.randomUUID());

        DishImage dishImage = new DishImage();
        dishImage.setImage(image);

        when(dishImageJpaRepository.findByDish_DishId(dishId)).thenReturn(List.of(dishImage));

        ImageResponseDTO responseDTO = new ImageResponseDTO();
        responseDTO.setId(image.getImageId());
        when(imageStructMapper.toDTO(image)).thenReturn(responseDTO);

        List<ImageResponseDTO> result = listDishImagesUseCase.execute(dishId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(responseDTO.getId(), result.get(0).getId());
    }

    @Test
    void shouldThrowEntityNotFoundWhenDishDoesNotExist() {
        when(dishJpaRepository.existsById(dishId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> listDishImagesUseCase.execute(dishId));
    }
}
