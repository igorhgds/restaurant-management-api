package henrique.igor.restaurantmanagementapi.services.storage;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {
    String upload(MultipartFile file, String path);
    void delete(String uri);
}
