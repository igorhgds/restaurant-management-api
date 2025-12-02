package henrique.igor.restaurantmanagementapi.mapper.user;

import henrique.igor.restaurantmanagementapi.entities.User;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.request.CreateUserRequestDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.response.MinimalUserResponseDTO;
import henrique.igor.restaurantmanagementapi.entities.dtos.user.response.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserStructMapper {

    @Mapping(target = "password", ignore = true)
    User toEntity(CreateUserRequestDTO userDto);

    UserResponseDTO toUserResponseDTO(User user);

    MinimalUserResponseDTO toMinimalUserResponseDTO(User user);
}
