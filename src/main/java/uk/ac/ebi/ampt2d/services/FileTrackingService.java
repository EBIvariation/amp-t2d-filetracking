/*
 * Copyright 2017 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.ebi.ampt2d.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * Service that implements the file tracking operations.
 */
@Service
public class FileTrackingService {

    private static final Logger logger = LoggerFactory.getLogger(FileTrackingService.class);

    private FileMetadataRepository fileMetadataRepository;

    private FileSystemStorageService fileSystemStorageService;

    @Autowired
    public FileTrackingService(FileMetadataRepository fileMetadataRepository, FileSystemStorageService
            fileSystemStorageService) {
        this.fileMetadataRepository = fileMetadataRepository;
        this.fileSystemStorageService = fileSystemStorageService;
    }

    public FileMetadata addFile(InputStream inputStream) throws StorageException, IOException {
        SourceFilePath sourceFilePath = fileSystemStorageService.store(inputStream);
        String fileHash = null;
        FileMetadata storedFileMetadata = null;
        try {
            storedFileMetadata = storeTemporalFileMetadata(sourceFilePath);
            fileHash = fileSystemStorageService.getFileHash(sourceFilePath);
            storedFileMetadata.setHash(fileHash);
            return fileMetadataRepository.save(storedFileMetadata);
        } catch (IOException e) {
            deleteFileMetadata(storedFileMetadata);
            throw e;
        } catch (DataIntegrityViolationException e) {
            // Duplicated file, we delete the entry and return the existing one.
            deleteFileMetadata(storedFileMetadata);
            return fileMetadataRepository.findByHash(fileHash);
        }
    }

    private void deleteFileMetadata(FileMetadata storedFileMetadata) {
        for(SourceFilePath sourceFilePath: storedFileMetadata.getSourceFilePaths()){
            fileSystemStorageService.delete(sourceFilePath);
        }
        fileMetadataRepository.delete(storedFileMetadata);
    }

    private FileMetadata storeTemporalFileMetadata(SourceFilePath sourceFilePath) throws IOException {
        long fileSize = fileSystemStorageService.getFileSize(sourceFilePath);
        FileMetadata tempFileMetadata = new FileMetadata(FileType.BINARY, fileSize);
        tempFileMetadata.addSourceFilePaths(sourceFilePath);
        return fileMetadataRepository.save(tempFileMetadata);
    }

}
