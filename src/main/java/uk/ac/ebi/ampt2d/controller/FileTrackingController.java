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
package uk.ac.ebi.ampt2d.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.ebi.ampt2d.Type;
import uk.ac.ebi.ampt2d.persistence.entities.FileMetadata;
import uk.ac.ebi.ampt2d.persistence.entities.SourceFilePath;
import uk.ac.ebi.ampt2d.persistence.repository.FileRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

@RestController
public class FileTrackingController {

    @Autowired
    private FileRepository fileRepository;

    @PostMapping("/upload-and-archive")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile multipartFile) {

        Path archivePath = fileRepository.archive(multipartFile);

        java.io.File storedFile = null;
        try {
            storedFile = convertMultipartToFile(multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO retrieve Type from request somehow
        FileMetadata fileMetadata = new FileMetadata(storedFile, Type.VCF, multipartFile.getName());

        Set<SourceFilePath> sourceFilePaths = new HashSet<>();
        sourceFilePaths.add(new SourceFilePath(fileMetadata, archivePath.toString()));

        fileMetadata.setSourceFilePaths(sourceFilePaths);

        fileRepository.save(fileMetadata);

        return new ResponseEntity<Path>(archivePath, HttpStatus.CREATED);
    }

    private java.io.File convertMultipartToFile(MultipartFile file) throws IOException {
        java.io.File convFile = new java.io.File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

}
