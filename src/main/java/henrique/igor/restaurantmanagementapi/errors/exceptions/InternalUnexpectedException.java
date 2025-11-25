package henrique.igor.restaurantmanagementapi.errors.exceptions;

import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import lombok.Getter;

@Getter

public class InternalUnexpectedException extends RuntimeException {
    private final ExceptionCode code;
    private final Object details;

    public InternalUnexpectedException(ExceptionCode code) {
        super(code.toString());
        this.code = code;
        this.details = null;
    }

    public InternalUnexpectedException(ExceptionCode code, Object details) {
        super(code.toString());
        this.code = code;
        this.details = details;
    }
}
