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
package uk.ac.ebi.ampt2d.persistence.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uk.ac.ebi.ampt2d.persistence.entities.SourceFilePath;

import javax.transaction.Transactional;

import static uk.ac.ebi.ampt2d.persistence.repository.SourceFilePathRepository.REST_REPOSITORY_FILE_PATHS;

/**
 * Spring generated repository for accessing SourceFilePath. This repository also includes the
 * {@link RepositoryRestResource} that configures spring to generate automatically a REST controller for the repository.
 */
@RepositoryRestResource(collectionResourceRel = "paths", path = REST_REPOSITORY_FILE_PATHS)
@Transactional
public interface SourceFilePathRepository extends PagingAndSortingRepository<SourceFilePath, Long> {

    String REST_REPOSITORY_FILE_PATHS = "/paths";

}
