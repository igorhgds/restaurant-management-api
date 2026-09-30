package henrique.igor.restaurantmanagementapi.repositories.order;

import henrique.igor.restaurantmanagementapi.entities.Order;
import henrique.igor.restaurantmanagementapi.entities.dtos.order.request.FindOrdersByFilterRequestDTO;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OrderSpecs {

    public static Specification<Order> byFilters(FindOrdersByFilterRequestDTO filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filters.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filters.getStatus()));
            }

            if (filters.getTableId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("table").get("tableId"), filters.getTableId()));
            }

            if (filters.getWaiterId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("waiter").get("userId"), filters.getWaiterId()));
            }

            if (filters.getStartDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), filters.getStartDate().atStartOfDay()));
            }

            if (filters.getEndDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), filters.getEndDate().plusDays(1).atStartOfDay()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
