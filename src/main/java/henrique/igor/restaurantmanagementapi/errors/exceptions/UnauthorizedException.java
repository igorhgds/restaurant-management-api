package henrique.igor.restaurantmanagementapi.errors.exceptions;

import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {
    private ExceptionCode code;

    public UnauthorizedException() {
        super(ExceptionCode.UNAUTHORIZED.name());
        this.code = ExceptionCode.UNAUTHORIZED;
    }

    public UnauthorizedException(Throwable cause) {
        super(ExceptionCode.UNAUTHORIZED.name(), cause);
        this.code = ExceptionCode.UNAUTHORIZED;
    }

    public UnauthorizedException(ExceptionCode code) {
        super(code.name());
        this.code = code;
    }

    public UnauthorizedException(ExceptionCode code, Throwable cause) {
        super(code.name(), cause);
        this.code = code;
    }
}