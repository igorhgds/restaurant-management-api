package henrique.igor.restaurantmanagementapi.errors.exceptions;

import lombok.*;

@Getter
@Setter
@With
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private String code;
    private String reason;
    private Object details = null;
}
