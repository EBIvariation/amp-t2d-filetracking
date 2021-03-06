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
            fileSystemStorageService){
        this.fileMetadataRepository = fileMetadataRepository;
        this.fileSystemStorageService = fileSystemStorageService;
    }

    public FileMetadata addFile(InputStream inputStream) throws StorageException, IOException {
        SourceFilePath sourceFilePath = fileSystemStorageService.store(inputStream);
        String fileHash = null;
        try {
            fileHash = fileSystemStorageService.getFileHash(sourceFilePath);
            long fileSize = fileSystemStorageService.getFileSize(sourceFilePath);

            FileMetadata fileMetadata = new FileMetadata(fileHash, FileType.BINARY, fileSize);
            fileMetadata.addSourceFilePaths(sourceFilePath);
            return fileMetadataRepository.save(fileMetadata);
        } catch (IOException e){
            fileSystemStorageService.delete(sourceFilePath);
            throw e;
        } catch (DataIntegrityViolationException e) {
            // Duplicated file, we delete the last stored.
            fileSystemStorageService.delete(sourceFilePath);
            return fileMetadataRepository.findByHash(fileHash);
        }
    }

}
