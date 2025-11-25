package henrique.igor.restaurantmanagementapi.security.dto;

import henrique.igor.restaurantmanagementapi.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@AllArgsConstructor
public class UserDetailsDTO implements UserDetails {

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.user.getUserRole().name()));
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    public Map<String, ?> toMap(){
        return Map.of(
                "id", this.user.getUserId().toString(),
                "username", this.user.getUsername(),
                "role", this.user.getUserRole().name()
        );
    }
}
