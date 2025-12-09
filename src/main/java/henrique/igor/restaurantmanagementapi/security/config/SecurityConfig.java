package henrique.igor.restaurantmanagementapi.security.config;

import henrique.igor.restaurantmanagementapi.security.dto.RouteDTO;
import henrique.igor.restaurantmanagementapi.security.filters.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

import static henrique.igor.restaurantmanagementapi.enums.UserRole.ADMIN;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationFilter authenticationFilter;

    private static final String[] SWAGGER_RESOURCES = {
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/docs/**"
    };

    private static final RouteDTO PUBLIC_ROUTES = new RouteDTO()
            .setPaths(POST, List.of(
                    "/auth/login"
            ))
            .setPaths(PATCH, List.of(
                    "/auth/login",
                    "/auth/generate-password-recovery-code",
                    "/auth/change-password",
                    "/auth/activate"
            ))
            .setPaths(GET, List.of(

            ));

    private static final RouteDTO PRIVATE_ROUTES = new RouteDTO()
            .setPaths(GET, List.of(
                    "/users/{userId}",
                    "/users/list-users"
            ))
            .setPaths(POST, List.of(
                    "/users/create"
            ))
            .setPaths(PATCH, List.of(
                    "/users/update"
            ))
            .setPaths(DELETE, List.of(
                    "/users/delete/{userId}"
            ));

    private static final RouteDTO ADMIN_ROUTES = new RouteDTO()
            .setRoles(ADMIN)
            .setPaths(GET, List.of(

            ));

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(POST, PUBLIC_ROUTES.getPathsByMethod(POST)).permitAll()
                        .requestMatchers(PATCH, PUBLIC_ROUTES.getPathsByMethod(PATCH)).permitAll()

                        .requestMatchers(GET, PRIVATE_ROUTES.getPathsByMethod(GET)).authenticated()
                        .requestMatchers(POST, PRIVATE_ROUTES.getPathsByMethod(POST)).authenticated()
                        .requestMatchers(PATCH, PRIVATE_ROUTES.getPathsByMethod(PATCH)).authenticated()
                        .requestMatchers(DELETE, PRIVATE_ROUTES.getPathsByMethod(DELETE)).authenticated()

                        .requestMatchers(SWAGGER_RESOURCES).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
