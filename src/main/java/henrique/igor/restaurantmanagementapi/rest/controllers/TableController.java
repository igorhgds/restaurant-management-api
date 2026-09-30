package henrique.igor.restaurantmanagementapi.rest.controllers;

import henrique.igor.restaurantmanagementapi.entities.dtos.table.request.CreateTableRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.table.request.FindTablesByFilterRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.table.request.UpdateTableRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.table.request.UpdateTableStatusRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.table.response.TableResponseDTO;
import henrique.igor.restaurantmanagementapi.rest.specs.TableControllerSpecs;
import henrique.igor.restaurantmanagementapi.usecases.table.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/tables")
@RequiredArgsConstructor
public class TableController implements TableControllerSpecs {

    private final CreateTableUseCase createTableUseCase;
    private final FindTableByIdUseCase findTableByIdUseCase;
    private final UpdateTableUseCase updateTableUseCase;
    private final UpdateTableStatusUseCase updateTableStatusUseCase;
    private final ListTablesUseCase listTablesUseCase;
    private final FindTablesByFilterUseCase findTablesByFilterUseCase;
    private final DeleteTableByIdUseCase deleteTableByIdUseCase;

    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TableResponseDTO> createTable(@RequestBody CreateTableRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createTableUseCase.execute(requestDTO));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<TableResponseDTO> findTableById(@PathVariable UUID id) {
        return ResponseEntity.ok(findTableByIdUseCase.execute(id));
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TableResponseDTO> updateTable(@PathVariable UUID id, @RequestBody UpdateTableRequestDTO requestDTO) {
        return ResponseEntity.ok(updateTableUseCase.execute(id, requestDTO));
    }

    @Override
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'WAITER')")
    public ResponseEntity<TableResponseDTO> updateTableStatus(@PathVariable UUID id, @RequestBody UpdateTableStatusRequestDTO requestDTO) {
        return ResponseEntity.ok(updateTableStatusUseCase.execute(id, requestDTO));
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<TableResponseDTO>> listTables(Pageable pageable) {
        return ResponseEntity.ok(listTablesUseCase.execute(pageable));
    }

    @Override
    @GetMapping("/filter")
    public ResponseEntity<Page<TableResponseDTO>> findTablesByFilter(@ModelAttribute FindTablesByFilterRequestDTO requestDTO, Pageable pageable) {
        return ResponseEntity.ok(findTablesByFilterUseCase.execute(requestDTO, pageable));
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> deleteTableById(@PathVariable UUID id) {
        deleteTableByIdUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
