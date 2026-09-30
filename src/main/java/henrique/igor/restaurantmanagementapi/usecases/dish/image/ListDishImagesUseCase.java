package henrique.igor.restaurantmanagementapi.usecases.dish.image;

import henrique.igor.restaurantmanagementapi.entities.Dish;
import henrique.igor.restaurantmanagementapi.entities.DishImage;
import henrique.igor.restaurantmanagementapi.entities.dtos.image.ImageResponseDTO;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.mapper.image.ImageStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.dish.DishJpaRepository;
import henrique.igor.restaurantmanagementapi.repositories.image.DishImageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListDishImagesUseCase {

    private final DishJpaRepository dishJpaRepository;
    private final DishImageJpaRepository dishImageJpaRepository;
    private final ImageStructMapper imageStructMapper;

    public List<ImageResponseDTO> execute(UUID dishId) {
        if (!dishJpaRepository.existsById(dishId)) {
            throw new EntityNotFoundException(Dish.class);
        }

        List<DishImage> dishImages = dishImageJpaRepository.findByDish_DishId(dishId);
        return dishImages.stream()
                .map(DishImage::getImage)
                .map(imageStructMapper::toDTO)
                .collect(Collectors.toList());
    }
}
