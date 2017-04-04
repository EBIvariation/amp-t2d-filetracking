package uk.ac.ebi.ampt2d.storage;

import org.springframework.web.multipart.MultipartFile;
import uk.ac.ebi.ampt2d.storage.exceptions.StorageException;

import java.nio.file.Path;

/**
 * Base interface to all the storage services
 */
public interface StorageService {

    Path store(MultipartFile file) throws StorageException;

    Path load(String filename);

}
