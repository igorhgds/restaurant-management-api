package henrique.igor.restaurantmanagementapi.repositories.dish;

import henrique.igor.restaurantmanagementapi.entities.Dish;
import henrique.igor.restaurantmanagementapi.entities.dtos.dish.request.FindDishesByFilterRequestDTO;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class DishSpecs {

    public static Specification<Dish> byFilters(FindDishesByFilterRequestDTO filters) {
        return (root, query, builder) -> {
            var predicates = new ArrayList<Predicate>();

            if (filters.getName() != null) {
                var dishName = builder.lower(root.get("name"));
                predicates.add(builder.like(dishName, "%" + filters.getName().toLowerCase() + "%"));
            }

            if (filters.getCategory() != null) {
                predicates.add(builder.equal(root.get("category"), filters.getCategory()));
            }

            if (filters.getIsEnabled() != null) {
                predicates.add(builder.equal(root.get("isEnabled"), filters.getIsEnabled()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}