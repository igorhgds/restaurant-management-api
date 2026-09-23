package henrique.igor.restaurantmanagementapi.repositories.menu;

import henrique.igor.restaurantmanagementapi.entities.Menu;
import henrique.igor.restaurantmanagementapi.entities.dtos.menu.request.FindMenusByFilterRequestDTO;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class MenuSpecs {

    public static Specification<Menu> byFilters(FindMenusByFilterRequestDTO filters) {
        return (root, query, builder) -> {
            var predicates = new ArrayList<Predicate>();

            if (filters.getName() != null) {
                var menuName = builder.lower(root.get("name"));
                predicates.add(builder.like(menuName, "%" + filters.getName().toLowerCase() + "%"));
            }

            if (filters.getActive() != null) {
                predicates.add(builder.equal(root.get("active"), filters.getActive()));
            }

            if (filters.getStartDateFrom() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("startDate"), filters.getStartDateFrom()));
            }

            if (filters.getStartDateTo() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("startDate"), filters.getStartDateTo()));
            }

            if (filters.getEndDateFrom() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("endDate"), filters.getEndDateFrom()));
            }

            if (filters.getEndDateTo() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("endDate"), filters.getEndDateTo()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}