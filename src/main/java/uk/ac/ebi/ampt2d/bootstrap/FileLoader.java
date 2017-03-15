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
package uk.ac.ebi.ampt2d.bootstrap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import uk.ac.ebi.ampt2d.FileType;
import uk.ac.ebi.ampt2d.persistence.entities.File;
import uk.ac.ebi.ampt2d.persistence.entities.SourceFilePath;
import uk.ac.ebi.ampt2d.persistence.repository.FileRepository;

@Component
public class FileLoader  implements ApplicationListener<ContextRefreshedEvent> {
    private Logger log = Logger.getLogger(FileLoader.class);

    private FileRepository fileRepository;

    @Autowired
    public void setFileRepository(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        File file1 = new File("abc", FileType.BAM, 10, null);
        fileRepository.save(file1);

        log.info("Saved File1");

        File file2 = new File("def", FileType.FATSQ, 15, null);
        fileRepository.save(file2);

        log.info("Saved File2");

        File file3 = new File(new java.io.File("/Users/diego/projects/T2D/jakku.covar.fromMysql.ped"), FileType.VCF, null);
        fileRepository.save(file3);

        log.info("Saved File3");
    }

}
