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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.ControllerUtils;
import org.springframework.data.rest.webmvc.HttpHeadersPreparer;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uk.ac.ebi.ampt2d.persistence.entities.FileMetadata;
import uk.ac.ebi.ampt2d.persistence.repository.FileMetadataRepository;

import static uk.ac.ebi.ampt2d.persistence.repository.FileMetadataRepository.REST_REPOSITORY_FILES;

/**
 * Repository that overrides the default behaviour for the FileMetadata post.
 * <p>
 * It is defined as {@link RepositoryRestController} instead of rest controller so that it can use properly the
 * default configuration of spring data and heper beans.
 */
@RepositoryRestController
public class FileMetadataRepositoryController {

    private final FileMetadataRepository fileMetadataRepository;

    private final HttpHeadersPreparer headersPreparer;

    /**
     * Constructor injection of the controller bean. Please note the injection of {@link HttpHeadersPreparer} bean.
     * This class is a spring-rest helper bean that is used to generate the header in the REST message responses.
     *
     * @param fileMetadataRepository
     * @param headersPreparer
     */
    @Autowired
    public FileMetadataRepositoryController(FileMetadataRepository fileMetadataRepository,
                                            HttpHeadersPreparer headersPreparer) {
        this.fileMetadataRepository = fileMetadataRepository;
        this.headersPreparer = headersPreparer;
    }

    /**
     * Proper method that handles POST operation of {@link FileMetadata}. It uses
     * {@link PersistentEntityResourceAssembler} to generate the Hateoas messages. In Spring Hateoas you can create
     * the message manually or externalize it to a {@link org.springframework.hateoas.ResourceAssembler}. Spring data
     * rest uses the {@link PersistentEntityResourceAssembler} as a generic assembler to generate the appropriate
     * Hateoas body message. Using this class ensures that we generate the same message as the standard repository.
     *
     * @param fileMetadata
     * @param assembler
     * @return
     */
    @PostMapping(REST_REPOSITORY_FILES)
    public ResponseEntity<?> fileMetadataPost(@RequestBody FileMetadata fileMetadata,
                                              final PersistentEntityResourceAssembler assembler) {
        FileMetadata storedFileMetadata;
        try {
            storedFileMetadata = fileMetadataRepository.save(fileMetadata);
        } catch (DataIntegrityViolationException e) {
            storedFileMetadata = fileMetadataRepository.findByHash(fileMetadata.getHash());
        }
        PersistentEntityResource resource = assembler.toResource(storedFileMetadata);
        //Controller utils is a helper class used to build Response entities with a specific status, header and resource
        return ControllerUtils.toResponseEntity(HttpStatus.CREATED, headersPreparer.prepareHeaders(resource), resource);
    }
}
