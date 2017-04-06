package uk.ac.ebi.ampt2d.storage;

import org.springframework.core.io.InputStreamSource;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.ebi.ampt2d.persistence.entities.SourceFilePath;
import uk.ac.ebi.ampt2d.storage.exceptions.StorageException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

/**
 * Base interface to all the storage services
 */
public interface StorageService {

    SourceFilePath store(InputStreamSource file) throws StorageException;

    String getFileHash(SourceFilePath sourceFilePath) throws IOException;

    long getFileSize(SourceFilePath sourceFilePath) throws IOException;
}
