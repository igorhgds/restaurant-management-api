package henrique.igor.restaurantmanagementapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DishImageId implements Serializable {

    @Column(name = "dish_id")
    private UUID dishId;

    @Column(name = "image_id")
    private UUID imageId;
}
