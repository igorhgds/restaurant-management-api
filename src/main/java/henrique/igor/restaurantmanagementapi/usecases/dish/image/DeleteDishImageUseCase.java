package henrique.igor.restaurantmanagementapi.usecases.dish.image;

import henrique.igor.restaurantmanagementapi.entities.Dish;
import henrique.igor.restaurantmanagementapi.entities.DishImage;
import henrique.igor.restaurantmanagementapi.entities.DishImageId;
import henrique.igor.restaurantmanagementapi.entities.Image;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.repositories.dish.DishJpaRepository;
import henrique.igor.restaurantmanagementapi.repositories.image.DishImageJpaRepository;
import henrique.igor.restaurantmanagementapi.repositories.image.ImageJpaRepository;
import henrique.igor.restaurantmanagementapi.services.storage.ImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteDishImageUseCase {

    private final DishJpaRepository dishJpaRepository;
    private final ImageJpaRepository imageJpaRepository;
    private final DishImageJpaRepository dishImageJpaRepository;
    private final ImageStorageService imageStorageService;

    @Transactional
    public void execute(UUID dishId, UUID imageId) {
        if (!dishJpaRepository.existsById(dishId)) {
            throw new EntityNotFoundException(Dish.class);
        }

        DishImageId id = new DishImageId(dishId, imageId);
        DishImage dishImage = dishImageJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(DishImage.class));

        Image image = dishImage.getImage();

        dishImageJpaRepository.delete(dishImage);
        imageJpaRepository.delete(image);
        imageStorageService.delete(image.getUri());
    }
}
