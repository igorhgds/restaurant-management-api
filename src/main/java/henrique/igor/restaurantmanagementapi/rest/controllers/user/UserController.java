package henrique.igor.restaurantmanagementapi.rest.controllers.user;

import henrique.igor.restaurantmanagementapi.entities.dtos.user.request.CreateUserRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.response.MinimalUserResponseDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.response.UserResponseDTO;
import henrique.igor.restaurantmanagementapi.rest.specs.UserControllerSpecs;
import henrique.igor.restaurantmanagementapi.usecases.user.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements UserControllerSpecs {

    private final CreateUserUseCase createUserUseCase;
    private final DeleteUserByIdUseCase deleteUserByIdUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;

    @PostMapping("/create")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid CreateUserRequestDTO request){
        UserResponseDTO response = createUserUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/delete/{userId}")
    public void delete(@PathVariable UUID userId){
        deleteUserByIdUseCase.execute(userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<MinimalUserResponseDTO> findUserById(@PathVariable UUID userId){
        MinimalUserResponseDTO response = findUserByIdUseCase.execute(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
