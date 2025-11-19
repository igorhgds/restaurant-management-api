package henrique.igor.restaurantmanagementapi.errors.exceptions;

import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import lombok.Getter;

@Getter
public class BusinessRuleException extends RuntimeException {

    private final ExceptionCode code;
    private final Object details;

    public BusinessRuleException(ExceptionCode code) {
        super(code.toString());
        this.code = code;
        this.details = null;
    }

    public BusinessRuleException(ExceptionCode code, Object details) {
        super();
        this.code = code;
        this.details = details;
    }
}
