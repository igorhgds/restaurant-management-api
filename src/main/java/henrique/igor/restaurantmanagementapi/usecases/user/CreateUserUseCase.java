package henrique.igor.restaurantmanagementapi.usecases.user;

import henrique.igor.restaurantmanagementapi.entities.User;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.request.CreateUserRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.response.UserResponseDTO;
import henrique.igor.restaurantmanagementapi.mapper.user.UserStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.user.UserJpaRepository;
import henrique.igor.restaurantmanagementapi.services.RandomCodeService;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserJpaRepository userRepository;
    private final RandomCodeService randomCodeService;
    private final UserStructMapper userMapper;
    // TODO -> create BCryptPassword

    public UserResponseDTO createUser(CreateUserRequestDTO request) {
        if(userRepository.findByUsername(request.username()).isPresent()) {
            throw new EntityExistsException();
        }

        var temporaryPassword = randomCodeService.generate(8);

        User user = userMapper.toEntity(request);
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setUserRole(request.userRole());
        user.setPassword(temporaryPassword);

        return userMapper.toUserResponseDTO(userRepository.save(user));
    }
}