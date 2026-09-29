package henrique.igor.restaurantmanagementapi.rest.specs;

import henrique.igor.restaurantmanagementapi.entities.dtos.menu.request.CreateMenuRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.menu.request.FindMenusByFilterRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.menu.request.UpdateMenuRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.menu.response.MenuResponseDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.pagination.PageableResponseDTO;
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

@Tag(name = "Menu", description = "Menus operations")
public interface MenuControllerSpecs {

    @Operation(summary = "Create menu")
    @ApiResponse(responseCode = "201", description = "Menu created successfully.",
            content = @Content(schema = @Schema(implementation = MenuResponseDTO.class)))
    @ApiResponseBusinessRuleException
    ResponseEntity<MenuResponseDTO> createMenu(@RequestBody @Valid CreateMenuRequestDTO request);

    @Operation(summary = "Update menu")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponseBadRequest
    ResponseEntity<Void> updateMenu(@PathVariable UUID id, @RequestBody @Valid UpdateMenuRequestDTO request);

    @Operation(summary = "Find menu by id")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = MenuResponseDTO.class)))
    @ApiResponseBadRequest
    ResponseEntity<MenuResponseDTO> findMenu(@PathVariable UUID id);

    @Operation(summary = "List all menus")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = MenuResponseDTO.class)))
    @ApiResponseBadRequest
    ResponseEntity<List<MenuResponseDTO>> listMenus();

    @Operation(summary = "Find menus by filters with pagination")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = PageableResponseDTO.class)))
    @ApiResponseBadRequest
    ResponseEntity<PageableResponseDTO<MenuResponseDTO>> findByFilter(
            @ParameterObject @ModelAttribute @Valid FindMenusByFilterRequestDTO request
    );

    @Operation(summary = "Delete menu")
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponseBadRequest
    ResponseEntity<Void> deleteMenu(@PathVariable UUID id);
}
