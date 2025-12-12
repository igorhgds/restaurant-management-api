package henrique.igor.restaurantmanagementapi.rest.controllers;

import henrique.igor.restaurantmanagementapi.entities.dtos.dish.request.CreateDishRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.dish.response.DishResponseDTO;
import henrique.igor.restaurantmanagementapi.rest.specs.DishControllerSpecs;
import henrique.igor.restaurantmanagementapi.usecases.dish.CreateDishUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor
public class DishController implements DishControllerSpecs {

    private final CreateDishUseCase createDishUseCase;

    @PostMapping
    public ResponseEntity<DishResponseDTO> createDish(@RequestBody @Valid CreateDishRequestDTO request){
        DishResponseDTO response = createDishUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
