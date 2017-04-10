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
package uk.ac.ebi.ampt2d.storage;

import com.google.common.hash.Hashing;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ampt2d.persistence.entities.SourceFilePath;
import uk.ac.ebi.ampt2d.storage.exceptions.StorageException;

import java.io.IOException;
import java.io.InputStream;
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
    public SourceFilePath store(InputStream inputStream) throws StorageException {
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
            Files.copy(inputStream, fileArchivePath);

            Path relativePath = Paths.get(storageProperties.getLocation()).relativize(fileArchivePath);

            return new SourceFilePath(relativePath.toString());
        } catch (IOException e) {
            throw new StorageException("Failed to store file", e);
        }
    }

    @Override
    public void remove(SourceFilePath sourceFilePath) {
        resolveFilePath(sourceFilePath).toFile().delete();
    }

    @Override
    public String getFileHash(SourceFilePath sourceFilePath) throws IOException {
        return com.google.common.io.Files.hash(resolveFilePath(sourceFilePath).toFile(), Hashing.sha384()).toString();
    }

    @Override
    public long getFileSize(SourceFilePath sourceFilePath) throws IOException {
        return Files.size(resolveFilePath(sourceFilePath));
    }

    private Path resolveFilePath(SourceFilePath sourceFilePath) {
        return Paths.get(storageProperties.getLocation()).resolve(sourceFilePath.getPath());
    }

}
