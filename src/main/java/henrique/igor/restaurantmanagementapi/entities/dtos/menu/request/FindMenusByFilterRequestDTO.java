package henrique.igor.restaurantmanagementapi.entities.dtos.menu.request;

import henrique.igor.restaurantmanagementapi.entities.dtos.pagination.PageableRequestDTO;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindMenusByFilterRequestDTO extends PageableRequestDTO {
    private String name;
    private Boolean active;
    private LocalDateTime startDateFrom;
    private LocalDateTime startDateTo;
    private LocalDateTime endDateFrom;
    private LocalDateTime endDateTo;
}