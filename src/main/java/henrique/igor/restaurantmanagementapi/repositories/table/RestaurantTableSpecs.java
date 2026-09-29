package henrique.igor.restaurantmanagementapi.repositories.table;

import henrique.igor.restaurantmanagementapi.entities.RestaurantTable;
import henrique.igor.restaurantmanagementapi.entities.dtos.table.request.FindTablesByFilterRequestDTO;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RestaurantTableSpecs {

    public static Specification<RestaurantTable> byFilter(FindTablesByFilterRequestDTO filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getNumber() != null) {
                predicates.add(cb.equal(root.get("number"), filter.getNumber()));
            }
            if (filter.getMinCapacity() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("capacity"), filter.getMinCapacity()));
            }
            if (filter.getMaxCapacity() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("capacity"), filter.getMaxCapacity()));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            if (filter.getLocation() != null) {
                predicates.add(cb.equal(root.get("location"), filter.getLocation()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
