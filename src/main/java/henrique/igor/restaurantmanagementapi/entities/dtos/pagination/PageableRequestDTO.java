package henrique.igor.restaurantmanagementapi.entities.dtos.pagination;

import jakarta.validation.constraints.Positive;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
public class PageableRequestDTO {

    @Getter
    @Positive
    private Integer limit = 10;

    private Integer page = 0;

    public Integer getPage() {
        this.page = (this.page - 1);
        return this.page < 0 ? 0 : this.page;
    }
}