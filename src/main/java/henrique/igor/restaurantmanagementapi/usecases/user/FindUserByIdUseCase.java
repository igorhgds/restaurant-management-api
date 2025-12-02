package henrique.igor.restaurantmanagementapi.usecases.user;

import henrique.igor.restaurantmanagementapi.entities.User;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.response.MinimalUserResponseDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.response.UserResponseDTO;
import henrique.igor.restaurantmanagementapi.errors.exceptions.EntityNotFoundException;
import henrique.igor.restaurantmanagementapi.mapper.user.UserStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindUserByIdUseCase {

    private final UserJpaRepository userRepository;
    private final UserStructMapper userMapper;

    public MinimalUserResponseDTO execute(UUID userId){
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class));

        return userMapper.toMinimalUserResponseDTO(user);
    }
}
