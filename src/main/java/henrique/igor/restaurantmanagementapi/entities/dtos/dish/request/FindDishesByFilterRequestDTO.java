package henrique.igor.restaurantmanagementapi.entities.dtos.dish.request;

import henrique.igor.restaurantmanagementapi.entities.dtos.pagination.PageableRequestDTO;
import henrique.igor.restaurantmanagementapi.enums.Category;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindDishesByFilterRequestDTO extends PageableRequestDTO{
    private String name;
    private Category category;
    private Boolean isEnabled;
}

