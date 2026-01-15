package henrique.igor.restaurantmanagementapi.util;

import henrique.igor.restaurantmanagementapi.enums.UserRole;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ValidateRoleHierarchy {

    public void execute(UserRole loggedUserRole, UserRole targetUserRole) {
        if (loggedUserRole == UserRole.ADMIN) return;

        if (loggedUserRole.getLevel() <= targetUserRole.getLevel()) {
            throw new BusinessRuleException(ExceptionCode.FORBIDDEN);
        }
    }

    public List<UserRole> getVisibleRoles(UserRole loggedUserRole) {
        return Arrays.stream(UserRole.values())
                .filter(role -> role.getLevel() < loggedUserRole.getLevel())
                .toList();
    }
}
