package uk.ac.ebi.ampt2d.storage;

import uk.ac.ebi.ampt2d.persistence.entities.SourceFilePath;
import uk.ac.ebi.ampt2d.storage.exceptions.StorageException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Base interface to all the storage services
 */
public interface StorageService {

    SourceFilePath store(InputStream inputStream) throws StorageException;

    void remove(SourceFilePath sourceFilePath);

    String getFileHash(SourceFilePath sourceFilePath) throws IOException;

    long getFileSize(SourceFilePath sourceFilePath) throws IOException;
}
