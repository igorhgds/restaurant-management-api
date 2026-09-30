package henrique.igor.restaurantmanagementapi.usecases.dish.image;

import henrique.igor.restaurantmanagementapi.entities.DishImage;
import henrique.igor.restaurantmanagementapi.entities.DishImageId;
import henrique.igor.restaurantmanagementapi.entities.Image;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.repositories.dish.DishJpaRepository;
import henrique.igor.restaurantmanagementapi.repositories.image.DishImageJpaRepository;
import henrique.igor.restaurantmanagementapi.repositories.image.ImageJpaRepository;
import henrique.igor.restaurantmanagementapi.services.storage.ImageStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteDishImageUseCaseTest {

    @Mock
    private DishJpaRepository dishJpaRepository;
    @Mock
    private ImageJpaRepository imageJpaRepository;
    @Mock
    private DishImageJpaRepository dishImageJpaRepository;
    @Mock
    private ImageStorageService imageStorageService;

    @InjectMocks
    private DeleteDishImageUseCase deleteDishImageUseCase;

    private UUID dishId;
    private UUID imageId;
    private DishImage dishImage;
    private Image image;

    @BeforeEach
    void setUp() {
        dishId = UUID.randomUUID();
        imageId = UUID.randomUUID();

        image = new Image();
        image.setImageId(imageId);
        image.setUri("some/uri.png");

        dishImage = new DishImage();
        dishImage.setId(new DishImageId(dishId, imageId));
        dishImage.setImage(image);
    }

    @Test
    void shouldDeleteImageSuccessfully() {
        when(dishJpaRepository.existsById(dishId)).thenReturn(true);
        when(dishImageJpaRepository.findById(any(DishImageId.class))).thenReturn(Optional.of(dishImage));

        deleteDishImageUseCase.execute(dishId, imageId);

        verify(dishImageJpaRepository, times(1)).delete(dishImage);
        verify(imageJpaRepository, times(1)).delete(image);
        verify(imageStorageService, times(1)).delete(image.getUri());
    }

    @Test
    void shouldThrowEntityNotFoundWhenDishDoesNotExist() {
        when(dishJpaRepository.existsById(dishId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> deleteDishImageUseCase.execute(dishId, imageId));
    }

    @Test
    void shouldThrowEntityNotFoundWhenDishImageRelationDoesNotExist() {
        when(dishJpaRepository.existsById(dishId)).thenReturn(true);
        when(dishImageJpaRepository.findById(any(DishImageId.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> deleteDishImageUseCase.execute(dishId, imageId));
    }
}
