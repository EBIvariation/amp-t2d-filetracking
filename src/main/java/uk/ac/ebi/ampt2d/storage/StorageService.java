package uk.ac.ebi.ampt2d.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.ebi.ampt2d.storage.exceptions.StorageException;
import uk.ac.ebi.ampt2d.storage.exceptions.StorageFileNotFoundException;

import java.nio.file.Path;

public interface StorageService {

    void init() throws StorageException;

    Path store(MultipartFile file) throws StorageException;

    Path load(String filename);

    Resource loadAsResource(String filename) throws StorageFileNotFoundException;

    void deleteAll();

}
