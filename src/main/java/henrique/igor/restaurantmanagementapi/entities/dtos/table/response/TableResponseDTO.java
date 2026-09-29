package henrique.igor.restaurantmanagementapi.entities.dtos.table.response;

import henrique.igor.restaurantmanagementapi.enums.TableLocation;
import henrique.igor.restaurantmanagementapi.enums.TableStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableResponseDTO {
    private UUID id;
    private Integer number;
    private Integer capacity;
    private TableStatus status;
    private TableLocation location;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
