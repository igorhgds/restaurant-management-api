package henrique.igor.restaurantmanagementapi.rest.controllers;

import henrique.igor.restaurantmanagementapi.entities.dtos.dish.request.CreateDishRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.dish.request.FindDishesByFilterRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.dish.response.DishResponseDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.pagination.PageableResponseDTO;
import henrique.igor.restaurantmanagementapi.rest.specs.DishControllerSpecs;
import henrique.igor.restaurantmanagementapi.usecases.dish.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor
public class DishController implements DishControllerSpecs {

    private final CreateDishUseCase createDishUseCase;
    private final DeleteDishByIdUseCase deleteDishByIdUseCase;
    private final FindDishByIdUseCase findDishByIdUseCase;
    private final ListDishesUseCase listDishesUseCase;
    private final FindDishesByFilterUseCase findDishesByFilterUseCase;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<DishResponseDTO> createDish(@RequestBody @Valid CreateDishRequestDTO request){
        DishResponseDTO response = createDishUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{dishId}")
    public ResponseEntity<DishResponseDTO> findDish(@PathVariable UUID dishId){
        return ResponseEntity.ok(findDishByIdUseCase.execute(dishId));
    }

    @GetMapping
    public ResponseEntity<PageableResponseDTO<DishResponseDTO>> findByFilter(
            @ParameterObject @ModelAttribute @Valid FindDishesByFilterRequestDTO request) {
        return ResponseEntity.ok(this.findDishesByFilterUseCase.findByFilters(request));
    }

    @GetMapping("/list")
    public ResponseEntity<List<DishResponseDTO>> listDishes(){
        return ResponseEntity.ok(listDishesUseCase.execute());
    }

    @DeleteMapping("delele/{dishId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void delete(@PathVariable UUID dishId){
        deleteDishByIdUseCase.execute(dishId);
    }
}
