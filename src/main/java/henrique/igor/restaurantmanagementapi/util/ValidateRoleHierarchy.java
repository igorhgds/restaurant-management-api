package henrique.igor.restaurantmanagementapi.util;

import henrique.igor.restaurantmanagementapi.enums.UserRole;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import org.springframework.stereotype.Component;

@Component
public class ValidateRoleHierarchy {

    public void execute(UserRole loggedUserRole, UserRole targetUserRole){
        if (loggedUserRole.equals(UserRole.ADMIN)) return;
        if (loggedUserRole.equals(UserRole.MANAGER) && targetUserRole.equals(UserRole.WAITER)) return;
        throw new BusinessRuleException(ExceptionCode.FORBIDDEN);
    }
}
