package henrique.igor.restaurantmanagementapi.usecases.dish.image;

import henrique.igor.restaurantmanagementapi.entities.Dish;
import henrique.igor.restaurantmanagementapi.entities.DishImage;
import henrique.igor.restaurantmanagementapi.entities.Image;
import henrique.igor.restaurantmanagementapi.entities.dtos.image.ImageResponseDTO;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.mapper.image.ImageStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.dish.DishJpaRepository;
import henrique.igor.restaurantmanagementapi.repositories.image.DishImageJpaRepository;
import henrique.igor.restaurantmanagementapi.repositories.image.ImageJpaRepository;
import henrique.igor.restaurantmanagementapi.services.storage.ImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadDishImageUseCase {

    private final DishJpaRepository dishJpaRepository;
    private final ImageJpaRepository imageJpaRepository;
    private final DishImageJpaRepository dishImageJpaRepository;
    private final ImageStorageService imageStorageService;
    private final ImageStructMapper imageStructMapper;

    private static final List<String> ALLOWED_EXTENSIONS = List.of(".png", ".jpg", ".jpeg", ".webp");
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @Transactional
    public ImageResponseDTO execute(UUID dishId, MultipartFile file) {
        Dish dish = dishJpaRepository.findById(dishId)
                .orElseThrow(() -> new EntityNotFoundException(Dish.class));

        if (file.isEmpty()) {
            throw new BusinessRuleException(ExceptionCode.OPERATION_NOT_ALLOWED, "File is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessRuleException(ExceptionCode.OPERATION_NOT_ALLOWED, "File size exceeds 5MB limit");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new BusinessRuleException(ExceptionCode.OPERATION_NOT_ALLOWED, "Invalid file name");
        }

        String extension = "";
        int dotIndex = originalFilename.lastIndexOf(".");
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex).toLowerCase();
        }

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new BusinessRuleException(ExceptionCode.OPERATION_NOT_ALLOWED, "Invalid file extension");
        }

        String path = "dishes/" + dishId.toString();
        String uri = imageStorageService.upload(file, path);

        Image image = new Image();
        image.setUri(uri);
        image.setOriginalFilename(originalFilename);
        image = imageJpaRepository.save(image);

        DishImage dishImage = new DishImage();
        dishImage.setDish(dish);
        dishImage.setImage(image);
        dishImageJpaRepository.save(dishImage);

        return imageStructMapper.toDTO(image);
    }
}
