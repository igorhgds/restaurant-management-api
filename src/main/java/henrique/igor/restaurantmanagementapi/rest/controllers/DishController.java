package henrique.igor.restaurantmanagementapi.rest.controllers;

import henrique.igor.restaurantmanagementapi.entities.dtos.dish.request.CreateDishRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.dish.response.DishResponseDTO;
import henrique.igor.restaurantmanagementapi.rest.specs.DishControllerSpecs;
import henrique.igor.restaurantmanagementapi.usecases.dish.CreateDishUseCase;
import henrique.igor.restaurantmanagementapi.usecases.dish.DeleteDishByIdUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor
public class DishController implements DishControllerSpecs {

    private final CreateDishUseCase createDishUseCase;
    private final DeleteDishByIdUseCase deleteDishByIdUseCase;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<DishResponseDTO> createDish(@RequestBody @Valid CreateDishRequestDTO request){
        DishResponseDTO response = createDishUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("delele/{dishId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void delete(@PathVariable UUID dishId){
        deleteDishByIdUseCase.execute(dishId);
    }
}
