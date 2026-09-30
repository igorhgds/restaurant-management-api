package henrique.igor.restaurantmanagementapi.usecases.dish.image;

import henrique.igor.restaurantmanagementapi.entities.Dish;
import henrique.igor.restaurantmanagementapi.entities.DishImage;
import henrique.igor.restaurantmanagementapi.entities.Image;
import henrique.igor.restaurantmanagementapi.entities.dtos.image.ImageResponseDTO;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.mapper.image.ImageStructMapper;
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
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadDishImageUseCaseTest {

    @Mock
    private DishJpaRepository dishJpaRepository;
    @Mock
    private ImageJpaRepository imageJpaRepository;
    @Mock
    private DishImageJpaRepository dishImageJpaRepository;
    @Mock
    private ImageStorageService imageStorageService;
    @Mock
    private ImageStructMapper imageStructMapper;

    @InjectMocks
    private UploadDishImageUseCase uploadDishImageUseCase;

    private UUID dishId;
    private Dish dish;

    @BeforeEach
    void setUp() {
        dishId = UUID.randomUUID();
        dish = new Dish();
        dish.setDishId(dishId);
    }

    @Test
    void shouldUploadImageSuccessfully() {
        MockMultipartFile file = new MockMultipartFile("file", "image.png", "image/png", "dummy content".getBytes());

        when(dishJpaRepository.findById(dishId)).thenReturn(Optional.of(dish));
        when(imageStorageService.upload(any(), anyString())).thenReturn("dishes/" + dishId + "/uuid.png");

        Image image = new Image();
        image.setImageId(UUID.randomUUID());
        image.setUri("dishes/" + dishId + "/uuid.png");
        image.setOriginalFilename("image.png");
        when(imageJpaRepository.save(any(Image.class))).thenReturn(image);

        ImageResponseDTO responseDTO = new ImageResponseDTO();
        responseDTO.setId(image.getImageId());
        responseDTO.setUri(image.getUri());
        responseDTO.setOriginalFilename(image.getOriginalFilename());
        when(imageStructMapper.toDTO(image)).thenReturn(responseDTO);

        ImageResponseDTO result = uploadDishImageUseCase.execute(dishId, file);

        assertNotNull(result);
        assertEquals(image.getUri(), result.getUri());
        verify(dishImageJpaRepository, times(1)).save(any(DishImage.class));
    }

    @Test
    void shouldThrowEntityNotFoundWhenDishDoesNotExist() {
        MockMultipartFile file = new MockMultipartFile("file", "image.png", "image/png", "dummy content".getBytes());
        when(dishJpaRepository.findById(dishId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> uploadDishImageUseCase.execute(dishId, file));
    }

    @Test
    void shouldThrowBusinessRuleExceptionWhenFileIsEmpty() {
        MockMultipartFile file = new MockMultipartFile("file", "image.png", "image/png", new byte[0]);
        when(dishJpaRepository.findById(dishId)).thenReturn(Optional.of(dish));

        assertThrows(BusinessRuleException.class, () -> uploadDishImageUseCase.execute(dishId, file));
    }

    @Test
    void shouldThrowBusinessRuleExceptionWhenExtensionIsInvalid() {
        MockMultipartFile file = new MockMultipartFile("file", "document.pdf", "application/pdf", "dummy content".getBytes());
        when(dishJpaRepository.findById(dishId)).thenReturn(Optional.of(dish));

        assertThrows(BusinessRuleException.class, () -> uploadDishImageUseCase.execute(dishId, file));
    }
}
