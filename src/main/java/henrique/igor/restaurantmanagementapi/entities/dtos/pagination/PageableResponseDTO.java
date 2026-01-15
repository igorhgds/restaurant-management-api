package henrique.igor.restaurantmanagementapi.entities.dtos.pagination;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageableResponseDTO<T> {
    private List<T> content;
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;

    public static <T> PageableResponseDTO<T> from(Page<T> page) {
        return new PageableResponseDTO<>(
                page.getContent(),
                page.getNumber(),
                page.getContent().size(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
