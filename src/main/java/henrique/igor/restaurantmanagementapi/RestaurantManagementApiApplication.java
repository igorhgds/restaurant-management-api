package henrique.igor.restaurantmanagementapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.validation.annotation.Validated;

@SpringBootApplication
@EnableJpaAuditing
public class RestaurantManagementApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantManagementApiApplication.class, args);
    }

}
