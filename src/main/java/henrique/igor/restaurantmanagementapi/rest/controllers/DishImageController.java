package henrique.igor.restaurantmanagementapi.rest.controllers;

import henrique.igor.restaurantmanagementapi.entities.dtos.image.ImageResponseDTO;
import henrique.igor.restaurantmanagementapi.usecases.dish.image.DeleteDishImageUseCase;
import henrique.igor.restaurantmanagementapi.usecases.dish.image.ListDishImagesUseCase;
import henrique.igor.restaurantmanagementapi.usecases.dish.image.UploadDishImageUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/dishes/{dishId}/images")
@RequiredArgsConstructor
@Tag(name = "Dish Images", description = "Endpoints for managing dish images")
public class DishImageController {

    private final UploadDishImageUseCase uploadDishImageUseCase;
    private final DeleteDishImageUseCase deleteDishImageUseCase;
    private final ListDishImagesUseCase listDishImagesUseCase;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Upload image for a dish", description = "Allows ADMIN or MANAGER to upload an image for a specific dish")
    public ResponseEntity<ImageResponseDTO> uploadImage(
            @PathVariable UUID dishId,
            @RequestParam("file") MultipartFile file) {
        ImageResponseDTO response = uploadDishImageUseCase.execute(dishId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{imageId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Delete image of a dish", description = "Allows ADMIN or MANAGER to delete an image of a specific dish")
    public ResponseEntity<Void> deleteImage(
            @PathVariable UUID dishId,
            @PathVariable UUID imageId) {
        deleteDishImageUseCase.execute(dishId, imageId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "List all images of a dish", description = "Allows authenticated users to list all images of a specific dish")
    public ResponseEntity<List<ImageResponseDTO>> listImages(@PathVariable UUID dishId) {
        List<ImageResponseDTO> images = listDishImagesUseCase.execute(dishId);
        return ResponseEntity.ok(images);
    }
}
