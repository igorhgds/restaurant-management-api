package henrique.igor.restaurantmanagementapi.mapper.image;

import henrique.igor.restaurantmanagementapi.entities.Image;
import henrique.igor.restaurantmanagementapi.entities.dtos.image.ImageResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImageStructMapper {

    @Mapping(source = "imageId", target = "id")
    ImageResponseDTO toDTO(Image image);
}
