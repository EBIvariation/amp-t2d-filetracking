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
import uk.ac.ebi.ampt2d.persistence.entities.FileMetadata;

import javax.transaction.Transactional;

@RepositoryRestResource(collectionResourceRel = "files", path = "files")
@Transactional
public interface FileMetadataRepository extends PagingAndSortingRepository<FileMetadata, String> {

    FileMetadata findByHash(String hash);

}