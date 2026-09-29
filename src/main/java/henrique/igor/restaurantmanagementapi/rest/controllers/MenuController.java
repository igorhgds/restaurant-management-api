package henrique.igor.restaurantmanagementapi.rest.controllers;

import henrique.igor.restaurantmanagementapi.entities.dtos.menu.request.CreateMenuRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.menu.request.FindMenusByFilterRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.menu.request.UpdateMenuRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.menu.response.MenuResponseDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.pagination.PageableResponseDTO;
import henrique.igor.restaurantmanagementapi.rest.specs.MenuControllerSpecs;
import henrique.igor.restaurantmanagementapi.usecases.menu.*;
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
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuController implements MenuControllerSpecs {

    private final CreateMenuUseCase createMenuUseCase;
    private final UpdateMenuUseCase updateMenuUseCase;
    private final FindMenuByIdUseCase findMenuByIdUseCase;
    private final FindMenusByFilterUseCase findMenusByFilterUseCase;
    private final ListMenusUseCase listMenusUseCase;
    private final DeleteMenuByIdUseCase deleteMenuByIdUseCase;

    @Override
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<MenuResponseDTO> createMenu(@RequestBody @Valid CreateMenuRequestDTO request) {
        MenuResponseDTO response = createMenuUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> updateMenu(@PathVariable UUID id, @RequestBody @Valid UpdateMenuRequestDTO request) {
        updateMenuUseCase.execute(request, id);
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<MenuResponseDTO> findMenu(@PathVariable UUID id) {
        return ResponseEntity.ok(findMenuByIdUseCase.execute(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<MenuResponseDTO>> listMenus() {
        return ResponseEntity.ok(listMenusUseCase.execute());
    }

    @Override
    @GetMapping("/filter")
    public ResponseEntity<PageableResponseDTO<MenuResponseDTO>> findByFilter(
            @ParameterObject @ModelAttribute @Valid FindMenusByFilterRequestDTO request) {
        return ResponseEntity.ok(findMenusByFilterUseCase.execute(request));
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> deleteMenu(@PathVariable UUID id) {
        deleteMenuByIdUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
