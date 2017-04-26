package uk.ac.ebi.ampt2d.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RepositoryRestController
public class FileMetadataRepositoryController {

    private final FileMetadataRepository fileMetadataRepository;

    private final HttpHeadersPreparer headersPreparer;

    @Autowired
    public FileMetadataRepositoryController(FileMetadataRepository fileMetadataRepository,
                                            HttpHeadersPreparer headersPreparer) {
        this.fileMetadataRepository = fileMetadataRepository;
        this.headersPreparer = headersPreparer;
    }

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
        return ControllerUtils.toResponseEntity(HttpStatus.CREATED,headersPreparer.prepareHeaders(resource), resource);
    }
}
