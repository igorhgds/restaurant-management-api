package henrique.igor.restaurantmanagementapi.usecases.user;

import henrique.igor.restaurantmanagementapi.entities.User;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.response.MinimalUserResponseDTO;
import henrique.igor.restaurantmanagementapi.mapper.user.UserStructMapper;
import henrique.igor.restaurantmanagementapi.repositories.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListUsersUseCase {

    private final UserJpaRepository userRepository;
    private final UserStructMapper userMapper;

    public List<MinimalUserResponseDTO> execute(){
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(userMapper::toMinimalUserResponseDTO)
                .collect(Collectors.toList());
    }
}
