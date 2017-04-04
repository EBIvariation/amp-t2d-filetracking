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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.ebi.ampt2d.storage.StorageException;
import uk.ac.ebi.ampt2d.storage.StorageService;

import javax.transaction.Transactional;
import java.nio.file.Path;

@Repository
@Transactional
public class FileRepositoryImpl implements FileRepositoryCustom {

    @Autowired
    private StorageService storageService;

    @Override
    public Path archive(MultipartFile upload) throws StorageException {
        return storageService.store(upload);
    }
}
