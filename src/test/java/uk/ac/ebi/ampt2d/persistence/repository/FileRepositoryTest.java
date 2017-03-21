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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.ampt2d.FileType;
import uk.ac.ebi.ampt2d.persistence.entities.File;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Testing the basic CRUD operations
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FileRepositoryTest {
    @Autowired
    private FileRepository fileRepository;

    @Before
    public void setUp() throws Exception {
        fileRepository.deleteAll();

        File vcf = new File("vcf_hash", "vcf", FileType.VCF, 15, null);
        File bam = new File("bam_hash", "bam", FileType.BED, 20, null);
        File fastq = new File("fatsq_hash", "fatsq", FileType.FASTQ, 100, null);

        fileRepository.save(vcf);
        fileRepository.save(bam);
        fileRepository.save(fastq);
    }

    @Test
    public void loadFiles() {
        List<File> files = (ArrayList<File>) fileRepository.findAll();

        assertEquals("Did not get all files", 3, files.size());
    }

    @Test
    public void findFile() {
        List<File> files = fileRepository.findByHash("vcf_hash");

        assertEquals(1, files.size());

        File file = files.get(0);

        assertEquals("vcf", file.getName());
        assertNotNull(file.getCreatedDate());
        assertNotNull(file.getLastModifiedDate());
        assertTrue(fileRepository.exists("vcf_hash"));
    }

    @Test
    public void testCRUD() {
        // Create a new file
        File secondVcf = new File("new_vcf_hash", "vcf", FileType.VCF, 150, null);
        File persistedFile = fileRepository.save(secondVcf);
        LocalDateTime created = persistedFile.getCreatedDate();
        LocalDateTime modified = persistedFile.getLastModifiedDate();

        // Assert it was created
        List<File> foundFile = fileRepository.findByHash(secondVcf.getHash());
        assertEquals(secondVcf.getName(), foundFile.get(0).getName());

        // Edit it's name
        String newName = "new name";
        foundFile.get(0).setName(newName);

        // Save to the database
        fileRepository.save(foundFile.get(0));

        // Assert it updated
        List<File> updatedFiles = fileRepository.findByHash(secondVcf.getHash());
        File updatedFile = updatedFiles.get(0);
        assertEquals(newName, updatedFile.getName());
        assertEquals(secondVcf.getHash(), updatedFile.getHash());
        assertTrue(updatedFile.getCreatedDate().isAfter(created));
        assertTrue(updatedFile.getLastModifiedDate().isAfter(modified));

        // Delete file
        fileRepository.delete(updatedFiles);

        // Assert not found
        List<File> emptyFile = fileRepository.findByHash(secondVcf.getName());
        assertEquals(0, emptyFile.size());
    }
    
}
