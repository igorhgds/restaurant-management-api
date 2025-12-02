package henrique.igor.restaurantmanagementapi.rest.specs;

import henrique.igor.restaurantmanagementapi.entities.dtos.user.request.CreateUserRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.response.MinimalUserResponseDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.response.UserResponseDTO;
import henrique.igor.restaurantmanagementapi.rest.specs.commons.ApiResponseBadRequest;
import henrique.igor.restaurantmanagementapi.rest.specs.commons.ApiResponseBusinessRuleException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@Tag(name= "User", description = "Users operations")
public interface UserControllerSpecs {

    @Operation(summary = "Create user")
    @ApiResponse(responseCode = "200", description = "User created successfully.",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    @ApiResponseBusinessRuleException
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid CreateUserRequestDTO request);

    @Operation(summary = "Delete user")
    @ApiResponse(responseCode = "204", description = "ok")
    @ApiResponseBadRequest
    public void delete(@PathVariable UUID userId);

    @Operation(summary = "Find user by id")
    @ApiResponse(responseCode = "200", description = "User created successfully.",
            content = @Content(schema = @Schema(implementation = MinimalUserResponseDTO.class)))
    public ResponseEntity<MinimalUserResponseDTO> findUserById(@PathVariable UUID userId);

    @Operation(summary = "list all users")
    @ApiResponse(responseCode = "200", description = "Users list successfully.",
            content = @Content(schema = @Schema(implementation = MinimalUserResponseDTO.class)))
    @ApiResponseBadRequest
    public ResponseEntity<List<MinimalUserResponseDTO>> listUsers();
}
