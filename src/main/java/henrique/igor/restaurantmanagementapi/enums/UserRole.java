package henrique.igor.restaurantmanagementapi.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN(3),
    MANAGER(2),
    WAITER(1);

    private final int level;

    UserRole(int level) {
        this.level = level;
    }
}