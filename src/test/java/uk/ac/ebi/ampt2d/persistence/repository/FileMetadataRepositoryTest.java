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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.ampt2d.FileType;
import uk.ac.ebi.ampt2d.persistence.entities.FileMetadata;
import uk.ac.ebi.ampt2d.persistence.entities.SourceFilePath;
import uk.ac.ebi.ampt2d.storage.StorageProperties;
import uk.ac.ebi.test.utils.AuditedDataJpaTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;

/**
 * Testing the basic CRUD operations
 */
@RunWith(SpringRunner.class)
@AuditedDataJpaTest
public class FileMetadataRepositoryTest {

    public static final String RANDOMSTRING = "randomstring";

    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    @MockBean
    private StorageProperties storageProperties;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        FileMetadata vcf = new FileMetadata("vcf_hash", FileType.VCF, 15);
        vcf.addSourceFilePaths(new SourceFilePath("/vcf/file/path"));

        FileMetadata bam = new FileMetadata("bam_hash", FileType.BED, 20);
        bam.addSourceFilePaths(new SourceFilePath("/bam/file/path"));

        FileMetadata fastq = new FileMetadata("fastq_hash", FileType.FASTQ, 100);
        fastq.addSourceFilePaths(new SourceFilePath("/fastq/file/path"));

        fileMetadataRepository.save(vcf);
        fileMetadataRepository.save(bam);
        fileMetadataRepository.save(fastq);
    }

    private void configureTemporalFilePaths() {
        try {
            given(storageProperties.getLocation()).willReturn(testFolder.newFolder().getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("IOException while creating a new test temporal folder", e);
        }
    }

    @Test
    public void testLoadFiles() {
        configureTemporalFilePaths();

        List<FileMetadata> fileMetadatas = (ArrayList<FileMetadata>) fileMetadataRepository.findAll();
        assertEquals("Did not get all fileMetadatas", 3, fileMetadatas.size());
    }

    @Test
    public void testFindFile() {
        configureTemporalFilePaths();

        FileMetadata fileMetadata = fileMetadataRepository.findByHash("vcf_hash");

        assertNotNull(fileMetadata);
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
        configureTemporalFilePaths();

        // Create a new file
        FileMetadata secondVcf = new FileMetadata("new_vcf_hash", FileType.VCF, 150);
        fileMetadataRepository.save(secondVcf);

        // Assert it was created
        FileMetadata foundFileMetadata = fileMetadataRepository.findByHash(secondVcf.getHash());
        assertNotNull(foundFileMetadata);

        // Save to the database
        fileMetadataRepository.save(foundFileMetadata);

        // Assert it updated
        FileMetadata updatedFileMetadata = fileMetadataRepository.findByHash(secondVcf.getHash());
        assertEquals(secondVcf.getHash(), updatedFileMetadata.getHash());

        // Delete file
        fileMetadataRepository.delete(updatedFileMetadata);

        // Assert not found
        FileMetadata emptyFileMetadata = fileMetadataRepository.findByHash(RANDOMSTRING);
        assertNull(emptyFileMetadata);
    }

}
