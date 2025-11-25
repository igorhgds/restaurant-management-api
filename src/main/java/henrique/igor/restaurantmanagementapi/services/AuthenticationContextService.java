package henrique.igor.restaurantmanagementapi.services;

import henrique.igor.restaurantmanagementapi.entities.User;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.InternalUnexpectedException;
import henrique.igor.restaurantmanagementapi.security.dto.UserDetailsDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationContextService {

    public UserDetailsDTO getData() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof Optional)
            return this.handleOptionalData((Optional<UserDetailsDTO>) principal);

        if (principal instanceof UserDetailsDTO)
            return (UserDetailsDTO) principal;

        throw new InternalUnexpectedException(ExceptionCode.UNAUTHORIZED);
    }

    private UserDetailsDTO handleOptionalData(Optional<UserDetailsDTO> principal) {
        principal.orElseThrow(() -> new EntityNotFoundException(String.valueOf(UserDetailsDTO.class)));
        return principal.get();
    }

    public User getAutheticatedUser() {
        return this.getData().getUser();
    }

    public Boolean isLoggedUser(User user) {
        return this.getAutheticatedUser().getUserId().equals(user.getUserId());
    }
}
