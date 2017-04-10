package uk.ac.ebi.ampt2d.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ampt2d.FileType;
import uk.ac.ebi.ampt2d.persistence.entities.FileMetadata;
import uk.ac.ebi.ampt2d.persistence.entities.SourceFilePath;
import uk.ac.ebi.ampt2d.persistence.repository.FileMetadataRepository;
import uk.ac.ebi.ampt2d.storage.FileSystemStorageService;
import uk.ac.ebi.ampt2d.storage.exceptions.StorageException;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FileTrackingService {

    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    @Autowired
    private FileSystemStorageService fileSystemStorageService;

    public void addFileToTrackingService(InputStream inputStream) throws StorageException, IOException {
        SourceFilePath sourceFilePath = fileSystemStorageService.store(inputStream);
        try {
            String fileHash = fileSystemStorageService.getFileHash(sourceFilePath);
            long fileSize = fileSystemStorageService.getFileSize(sourceFilePath);

            FileMetadata fileMetadata = new FileMetadata(fileHash, FileType.BINARY, fileSize);
            fileMetadata.addSourceFilePaths(sourceFilePath);
            fileMetadataRepository.save(fileMetadata);
        } catch (IOException e){
            fileSystemStorageService.remove(sourceFilePath);
            throw e;
        } catch (DataIntegrityViolationException e) {
            // Duplicated file, we remove the new stored file.
            fileSystemStorageService.remove(sourceFilePath);
        }
    }

}
