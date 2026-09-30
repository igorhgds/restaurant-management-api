package henrique.igor.restaurantmanagementapi.entities.dtos.table.request;

import henrique.igor.restaurantmanagementapi.enums.TableLocation;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTableRequestDTO {
    private Integer number;

    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    private TableLocation location;
}
