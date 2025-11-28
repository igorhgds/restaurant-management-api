package henrique.igor.restaurantmanagementapi.errors.exceptions;

import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {
    private final Class<?> entityClass;

    public EntityNotFoundException(Class<?> entityClass) {
        super(ExceptionCode.ENTITY_NOT_FOUND.toString());
        this.entityClass = entityClass;
    }
}
