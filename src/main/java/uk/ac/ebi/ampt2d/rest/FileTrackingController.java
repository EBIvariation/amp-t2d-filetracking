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
package uk.ac.ebi.ampt2d.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.ebi.ampt2d.services.FileTrackingService;
import uk.ac.ebi.ampt2d.storage.exceptions.StorageException;

import java.io.IOException;

/**
 * Rest controller for the file tracking. This controller doesn't show the repository operations, that controller is
 * generated automatically by spring
 */
@RestController
public class FileTrackingController {

    private final Logger logger = LoggerFactory.getLogger(ExceptionHandling.class);

    @Autowired
    private FileTrackingService fileTrackingService;

    @PostMapping("/upload")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile multipartFile) throws
            IOException, StorageException {
        fileTrackingService.addFileToTrackingService(multipartFile.getInputStream());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
