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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ControllerUtils;
import org.springframework.data.rest.webmvc.HttpHeadersPreparer;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.ebi.ampt2d.persistence.entities.FileMetadata;
import uk.ac.ebi.ampt2d.services.FileTrackingService;
import uk.ac.ebi.ampt2d.storage.exceptions.StorageException;

import java.io.IOException;

/**
 * Rest controller for the file tracking. This controller doesn't show the repository operations, that controller is
 * generated automatically by spring
 */
@RepositoryRestController
@RequestMapping("/") //Required when using @RepositoryRestController (not stated in docs)
public class FileTrackingController {

    public static final String REST_UPLOAD = "/upload";

    private FileTrackingService fileTrackingService;

    private final HttpHeadersPreparer headersPreparer;

    @Autowired
    public FileTrackingController(FileTrackingService fileTrackingService, HttpHeadersPreparer headersPreparer) {
        this.fileTrackingService = fileTrackingService;
        this.headersPreparer = headersPreparer;
    }

    @PostMapping(REST_UPLOAD)
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile multipartFile ,
                                    final PersistentEntityResourceAssembler assembler)
            throws IOException, StorageException {
        FileMetadata fileMetadata = fileTrackingService.addFile(multipartFile.getInputStream());
        PersistentEntityResource resource = assembler.toResource(fileMetadata);
        return ControllerUtils.toResponseEntity(HttpStatus.CREATED,headersPreparer.prepareHeaders(resource), resource);
    }
}
