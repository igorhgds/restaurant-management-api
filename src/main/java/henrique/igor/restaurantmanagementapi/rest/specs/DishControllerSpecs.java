package henrique.igor.restaurantmanagementapi.rest.specs;

import henrique.igor.restaurantmanagementapi.entities.dtos.dish.request.*;
import henrique.igor.restaurantmanagementapi.entities.dtos.dish.response.DishResponseDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.pagination.PageableResponseDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.response.MinimalUserResponseDTO;
import henrique.igor.restaurantmanagementapi.rest.specs.commons.ApiResponseBadRequest;
import henrique.igor.restaurantmanagementapi.rest.specs.commons.ApiResponseBusinessRuleException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name= "Dish", description = "Dishes operations")
public interface DishControllerSpecs {

    @Operation(summary = "Create dish")
    @ApiResponse(responseCode = "201", description = "Dish created successfully.",
            content = @Content(schema = @Schema(implementation = DishResponseDTO.class)))
    @ApiResponseBusinessRuleException
    ResponseEntity<DishResponseDTO> createDish(@RequestBody @Valid CreateDishRequestDTO request);

    @Operation(summary = "Update dish")
    @ApiResponse(responseCode = "204", description = "ok")
    @ApiResponseBadRequest
    ResponseEntity<Void> update(@PathVariable UUID dishId, @RequestBody @Valid UpdateDishRequestDTO request);

    @Operation(summary = "Find dish by id")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = DishResponseDTO.class)))
    @ApiResponseBadRequest
    ResponseEntity<DishResponseDTO> findDish(@PathVariable UUID dishId);

    @Operation(summary = "Find dishes by filters with pagination")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = PageableResponseDTO.class)))
    @ApiResponseBadRequest
    ResponseEntity<PageableResponseDTO<DishResponseDTO>> findByFilter(
            @ParameterObject @ModelAttribute @Valid FindDishesByFilterRequestDTO request
    );

    @Operation(summary = "List all dishes")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = DishResponseDTO.class)))
    @ApiResponseBadRequest
    ResponseEntity<List<DishResponseDTO>> listDishes();

    @Operation(summary = "Delete dish")
    @ApiResponse(responseCode = "204", description = "ok")
    @ApiResponseBadRequest
    public ResponseEntity<Void> delete(@PathVariable UUID dishId);
}
