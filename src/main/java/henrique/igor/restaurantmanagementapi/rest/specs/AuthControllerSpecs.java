package henrique.igor.restaurantmanagementapi.rest.specs;

import henrique.igor.restaurantmanagementapi.entities.dtos.auth.request.*;
import henrique.igor.restaurantmanagementapi.entities.dtos.auth.response.LoginResponseDTO;
import henrique.igor.restaurantmanagementapi.rest.specs.commons.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name= "Auth", description = "Authentication operations")
public interface AuthControllerSpecs {

    @Operation(summary = "User login")
    @ApiResponse(responseCode = "200", description = "Login successful.",
            content = @Content(schema = @Schema(implementation = LoginResponseDTO.class)))
    @ApiResponseBadRequest
    @ApiResponseUnauthorized
    ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO request);

    @Operation(summary = "Activate user account")
    @ApiResponse(responseCode = "204", description = "Account successfully activated.")
    @ApiResponseBadRequest
    @ApiResponseBusinessRuleException
    void activateAccount(@RequestBody @Valid ActivateAccountRequestDTO request);

    @Operation(summary = "Generate password recovery code")
    @ApiResponse(responseCode = "204", description = "ok")
    @ApiResponseBadRequest
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void generatePasswordRecovery(@RequestBody @Valid GeneratePasswordRecoveryRequestDTO request);

    @Operation(summary = "Change password")
    @ApiResponse(responseCode = "204", description = "ok")
    @ApiResponseBadRequest
    @ApiResponseBusinessRuleException
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void changePassword(@RequestBody @Valid ChangePasswordRequestDTO request);
}
