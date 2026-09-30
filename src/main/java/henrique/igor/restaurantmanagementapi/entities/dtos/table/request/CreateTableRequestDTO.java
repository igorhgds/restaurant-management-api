package henrique.igor.restaurantmanagementapi.entities.dtos.table.request;

import henrique.igor.restaurantmanagementapi.enums.TableLocation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTableRequestDTO {
    @NotNull(message = "Number is required")
    private Integer number;

    @Min(value = 1, message = "Capacity must be at least 1")
    @NotNull(message = "Capacity is required")
    private Integer capacity;

    @NotNull(message = "Location is required")
    private TableLocation location;
}
