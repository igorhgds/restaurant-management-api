package henrique.igor.restaurantmanagementapi.usecases.user;

import henrique.igor.restaurantmanagementapi.entities.User;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.request.CreateUserRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.response.UserResponseDTO;
import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.mapper.user.UserStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.user.UserJpaRepository;
import henrique.igor.restaurantmanagementapi.services.RandomCodeService;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserJpaRepository userRepository;
    private final RandomCodeService randomCodeService;
    private final UserStructMapper userMapper;
    // TODO -> create BCryptPassword

    public UserResponseDTO createUser(CreateUserRequestDTO request) {
        Map<String, String> errors = new HashMap<>();

        if(userRepository.findByUsername(request.username()).isPresent())
            errors.put("username", "user.username.duplicate");

        if(userRepository.findByEmail(request.email()).isPresent())
            errors.put("email", "user.email.duplicate");

        if (!errors.isEmpty())
            throw new BusinessRuleException(ExceptionCode.DUPLICATED_RESOURCE, errors);


        var temporaryPassword = randomCodeService.generate(8);

        User user = userMapper.toEntity(request);
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setUserRole(request.userRole());
        user.setPassword(temporaryPassword);

        return userMapper.toUserResponseDTO(userRepository.save(user));
    }
}