package henrique.igor.restaurantmanagementapi.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "menu_dishes")
@IdClass(MenuDishId.class)
@EntityListeners(AuditingEntityListener.class)
public class MenuDish implements Serializable {

    @Id
    @Column(name = "menu_id", nullable = false)
    private UUID menuId;

    @Id
    @Column(name = "dish_id", nullable = false)
    private UUID dishId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", insertable = false, updatable = false)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", insertable = false, updatable = false)
    private Dish dish;
}