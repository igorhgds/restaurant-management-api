package henrique.igor.restaurantmanagementapi.entities.dtos.table.request;

import henrique.igor.restaurantmanagementapi.enums.TableStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTableStatusRequestDTO {
    @NotNull(message = "Status is required")
    private TableStatus status;
}
