package henrique.igor.restaurantmanagementapi.rest.specs;

import henrique.igor.restaurantmanagementapi.entities.dtos.dish.request.CreateDishRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.dish.response.DishResponseDTO;
import henrique.igor.restaurantmanagementapi.rest.specs.commons.ApiResponseBusinessRuleException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name= "Dish", description = "Dishes operations")
public interface DishControllerSpecs {

    @Operation(summary = "Create dish")
    @ApiResponse(responseCode = "201", description = "Dish created successfully.",
            content = @Content(schema = @Schema(implementation = DishResponseDTO.class)))
    @ApiResponseBusinessRuleException
    ResponseEntity<DishResponseDTO> createDish(@RequestBody @Valid CreateDishRequestDTO request);
}
