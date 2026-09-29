package henrique.igor.restaurantmanagementapi.entities.dtos.table.request;

import henrique.igor.restaurantmanagementapi.enums.TableLocation;
import henrique.igor.restaurantmanagementapi.enums.TableStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindTablesByFilterRequestDTO {
    private Integer number;
    private Integer minCapacity;
    private Integer maxCapacity;
    private TableStatus status;
    private TableLocation location;
}
