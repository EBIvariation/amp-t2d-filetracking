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
import uk.ac.ebi.ampt2d.persistence.entities.FileMetadata;
import uk.ac.ebi.ampt2d.persistence.entities.SourceFilePath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Testing the basic CRUD operations
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FileMetadataRepositoryTest {
    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    @Before
    public void setUp() throws Exception {
        fileMetadataRepository.deleteAll();

        FileMetadata vcf = new FileMetadata("vcf_hash", "vcf", FileType.VCF, 15);
        vcf.setSourceFilePaths(new HashSet<>(Collections.singletonList(new SourceFilePath(vcf, "/vcf/file/path"))));

        FileMetadata bam = new FileMetadata("bam_hash", "bam", FileType.BED, 20);
        bam.setSourceFilePaths(new HashSet<>(Collections.singletonList(new SourceFilePath(bam, "/bam/file/path"))));

        FileMetadata fastq = new FileMetadata("fastq_hash", "fastq", FileType.FASTQ, 100);
        fastq.setSourceFilePaths(
                new HashSet<>(Collections.singletonList(new SourceFilePath(fastq, "/fastq/file/path"))));

        fileMetadataRepository.save(vcf);
        fileMetadataRepository.save(bam);
        fileMetadataRepository.save(fastq);
    }

    @Test
    public void testLoadFiles() {
        List<FileMetadata> fileMetadatas = (ArrayList<FileMetadata>) fileMetadataRepository.findAll();
        assertEquals("Did not get all fileMetadatas", 3, fileMetadatas.size());
    }

    @Test
    public void testFindFile() {
        FileMetadata fileMetadata = fileMetadataRepository.findByHash("vcf_hash");

        assertNotNull(fileMetadata);
        assertEquals("vcf", fileMetadata.getName());
        assertNotNull(fileMetadata.getCreatedDate());
        assertNotNull(fileMetadata.getLastModifiedDate());

        Set<SourceFilePath> sourceFilePaths = fileMetadata.getSourceFilePaths();
        assertNotNull(sourceFilePaths);
        assertEquals("Did not get all source fileMetadata paths", 1, sourceFilePaths.size());

        SourceFilePath sourceFilePath = sourceFilePaths.iterator().next();
        assertEquals("/vcf/file/path", sourceFilePath.getPath());
        assertEquals(fileMetadata, sourceFilePath.getFileMetadata());
    }

    @Test
    public void testCRUDoperations() {
        // Create a new file
        FileMetadata secondVcf = new FileMetadata("new_vcf_hash", "vcf", FileType.VCF, 150);
        fileMetadataRepository.save(secondVcf);

        // Assert it was created
        FileMetadata foundFileMetadata = fileMetadataRepository.findByHash(secondVcf.getHash());
        assertEquals(secondVcf.getName(), foundFileMetadata.getName());

        // Edit it's name
        String newName = "new name";
        foundFileMetadata.setName(newName);

        // Save to the database
        fileMetadataRepository.save(foundFileMetadata);

        // Assert it updated
        FileMetadata updatedFileMetadata = fileMetadataRepository.findByHash(secondVcf.getHash());
        assertEquals(newName, updatedFileMetadata.getName());
        assertEquals(secondVcf.getHash(), updatedFileMetadata.getHash());

        // Delete file
        fileMetadataRepository.delete(updatedFileMetadata);

        // Assert not found
        FileMetadata emptyFileMetadata = fileMetadataRepository.findByHash(secondVcf.getName());
        assertNull(emptyFileMetadata);
    }

}
