package henrique.igor.restaurantmanagementapi.services.storage;

import henrique.igor.restaurantmanagementapi.errors.ExceptionCode;
import henrique.igor.restaurantmanagementapi.errors.exceptions.InternalUnexpectedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalStorageService implements ImageStorageService {

    private final String uploadDir;

    public LocalStorageService(@Value("${app.upload.dir:uploads/images}") String uploadDir) {
        this.uploadDir = uploadDir;
    }

    @Override
    public String upload(MultipartFile file, String path) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.lastIndexOf(".") > 0) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID().toString() + extension;

            Path directory = Paths.get(uploadDir, path);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            Path targetLocation = directory.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation);

            return Paths.get(path, fileName).toString().replace("\\", "/");
        } catch (IOException ex) {
            throw new InternalUnexpectedException(ExceptionCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void delete(String uri) {
        try {
            Path fileLocation = Paths.get(uploadDir, uri);
            Files.deleteIfExists(fileLocation);
        } catch (IOException ex) {
            throw new InternalUnexpectedException(ExceptionCode.INTERNAL_SERVER_ERROR);
        }
    }
}
