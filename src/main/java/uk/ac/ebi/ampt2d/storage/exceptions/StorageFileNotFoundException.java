package uk.ac.ebi.ampt2d.storage.exceptions;

import uk.ac.ebi.ampt2d.storage.exceptions.StorageException;

public class StorageFileNotFoundException extends StorageException {

    public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}