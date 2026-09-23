package henrique.igor.restaurantmanagementapi.entities;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MenuDishId implements Serializable {

    private UUID menuId;
    private UUID dishId;
}