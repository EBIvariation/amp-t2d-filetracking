package uk.ac.ebi.ampt2d.storage;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.ebi.ampt2d.storage.exceptions.StorageException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileSystemStorageService implements StorageService {

    private Logger logger = Logger.getLogger(FileSystemStorageService.class);

    private final Path rootLocation;

    public FileSystemStorageService(StorageProperties properties) {
        String location = properties.getLocation();
        assertPath(location);
        this.rootLocation = Paths.get(location);

    }

    private void assertPath(String location) {
        Assert.notNull(location, "Location must be defined in properties");
        Path path = Paths.get(location);
        Assert.isTrue(Files.exists(path), "Location doesn't exist");
        Assert.isTrue(Files.isDirectory(path), "Location is not a directory");
        Assert.isTrue(Files.isWritable(path), "Location doesn't allow to write");
        Assert.isTrue(Files.isReadable(path), "Location is not readable");
    }

    @Override
    public Path store(MultipartFile file) throws StorageException {
        Path archivePath;

        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
        }

        try {
            archivePath = this.rootLocation.resolve(file.getOriginalFilename());
            logger.debug("Archiving file "+ file.getOriginalFilename() +" into " + archivePath.toString());
            Files.copy(file.getInputStream(), archivePath);

            return archivePath;
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

}
