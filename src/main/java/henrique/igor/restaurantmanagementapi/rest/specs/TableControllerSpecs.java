package henrique.igor.restaurantmanagementapi.rest.specs;

import henrique.igor.restaurantmanagementapi.entities.dtos.table.request.CreateTableRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.table.request.FindTablesByFilterRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.table.request.UpdateTableRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.table.request.UpdateTableStatusRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.table.response.TableResponseDTO;
import henrique.igor.restaurantmanagementapi.rest.specs.commons.ApiResponseBusinessRuleException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Tag(name = "Tables", description = "Endpoints for restaurant table management")
public interface TableControllerSpecs {

    @Operation(summary = "Create a new table")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Table created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error or table number already exists")
    })
    @ApiResponseBusinessRuleException
    ResponseEntity<TableResponseDTO> createTable(@Valid @RequestBody CreateTableRequestDTO requestDTO);

    @Operation(summary = "Find a table by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Table found"),
            @ApiResponse(responseCode = "404", description = "Table not found")
    })
    ResponseEntity<TableResponseDTO> findTableById(@PathVariable UUID id);

    @Operation(summary = "Update a table")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Table updated successfully"),
            @ApiResponse(responseCode = "404", description = "Table not found"),
            @ApiResponse(responseCode = "400", description = "Validation error or table number already exists")
    })
    @ApiResponseBusinessRuleException
    ResponseEntity<TableResponseDTO> updateTable(@PathVariable UUID id, @Valid @RequestBody UpdateTableRequestDTO requestDTO);

    @Operation(summary = "Update a table status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Table status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Table not found")
    })
    ResponseEntity<TableResponseDTO> updateTableStatus(@PathVariable UUID id, @Valid @RequestBody UpdateTableStatusRequestDTO requestDTO);

    @Operation(summary = "List all tables")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of tables")
    })
    ResponseEntity<Page<TableResponseDTO>> listTables( Pageable pageable);

    @Operation(summary = "Find tables by filter")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of filtered tables")
    })
    ResponseEntity<Page<TableResponseDTO>> findTablesByFilter(FindTablesByFilterRequestDTO requestDTO,  Pageable pageable);

    @Operation(summary = "Delete a table by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Table deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Table not found"),
            @ApiResponse(responseCode = "400", description = "Table cannot be deleted (e.g. occupied)")
    })
    @ApiResponseBusinessRuleException
    ResponseEntity<Void> deleteTableById(@PathVariable UUID id);
}
