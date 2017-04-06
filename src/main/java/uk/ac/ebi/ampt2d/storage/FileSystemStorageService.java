package uk.ac.ebi.ampt2d.storage;

import com.google.common.hash.Hashing;
import org.apache.log4j.Logger;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ampt2d.persistence.entities.SourceFilePath;
import uk.ac.ebi.ampt2d.storage.exceptions.StorageException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FileSystemStorageService implements StorageService {

    private Logger logger = Logger.getLogger(FileSystemStorageService.class);

    private final StorageProperties storageProperties;

    public FileSystemStorageService(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
    }

    @Override
    public SourceFilePath store(InputStreamSource file) throws StorageException {
        LocalDateTime localDateTime = LocalDateTime.now();

        Path archivePath = Paths.get(storageProperties.getLocation(), Integer.toString(localDateTime.getYear()),
                Integer.toString(localDateTime.getMonthValue()), Integer.toString(localDateTime.getDayOfMonth()));

        try {
            Files.createDirectories(archivePath);
        } catch (IOException e) {
            final String message = "Error while creating archiving directory " + archivePath;
            logger.error(message, e);
            throw new StorageException(message);
        }

        try {
            Path fileArchivePath = archivePath.resolve(localDateTime.format(DateTimeFormatter.ofPattern("kk:mm:ss-N")));
            logger.debug("Archiving file into " + fileArchivePath.toString());
            Files.copy(file.getInputStream(), fileArchivePath);

            Path relativePath = Paths.get(storageProperties.getLocation()).relativize(fileArchivePath);

            return new SourceFilePath(relativePath.toString());
        } catch (IOException e) {
            throw new StorageException("Failed to store file", e);
        }
    }

    @Override
    public String getFileHash(SourceFilePath sourceFilePath) throws IOException {
        Path filePath = Paths.get(storageProperties.getLocation()).resolve(sourceFilePath.getPath());
        return com.google.common.io.Files.hash(filePath.toFile(), Hashing.sha384()).toString();
    }

    @Override
    public long getFileSize(SourceFilePath sourceFilePath) throws IOException {
        return Files.size(Paths.get(storageProperties.getLocation()).resolve(sourceFilePath.getPath()));
    }


}
