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
package uk.ac.ebi.ampt2d;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.Constants;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import uk.ac.ebi.ampt2d.configuration.RestTemplateConfiguration;
import uk.ac.ebi.ampt2d.persistence.entities.FileMetadata;
import uk.ac.ebi.ampt2d.rest.FileTrackingController;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static uk.ac.ebi.ampt2d.persistence.repository.FileMetadataRepository.REST_REPOSITORY_FILES;
import static uk.ac.ebi.ampt2d.rest.FileTrackingController.REST_UPLOAD;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class FileManagerApplicationTests {

    private static final String TEST_HASH = "asd123";
    private static final String TEST_HASH_REPEATED = "asd456";
    public static final String TEST_FILE = "/test.file";
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void fileUpload() throws URISyntaxException {
        File resource = new File(FileManagerApplicationTests.class.getResource(TEST_FILE).getFile());

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
        parts.add("file", new FileSystemResource(resource));

        ResponseEntity<Void> result = restTemplate.postForEntity(REST_UPLOAD, parts, Void.class);

        Assert.assertEquals(HttpStatus.CREATED,result.getStatusCode());
    }

    @Test
    public void uploadTwoTimesTheSameFile() throws URISyntaxException {
        File resource = new File(FileManagerApplicationTests.class.getResource(TEST_FILE).getFile());

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
        parts.add("file", new FileSystemResource(resource));

        ResponseEntity<Void> result = restTemplate.postForEntity(REST_UPLOAD, parts, Void.class);
        Assert.assertEquals(HttpStatus.CREATED,result.getStatusCode());
        result = restTemplate.postForEntity(REST_UPLOAD, parts, Void.class);
        Assert.assertEquals(HttpStatus.CREATED,result.getStatusCode());
    }

    @Test
    public void checkRestToRepository(){
        ResponseEntity<String> response = restTemplate.getForEntity(REST_REPOSITORY_FILES,String.class);
        Assert.assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void checkAddToRepository(){
        FileMetadata fileMetadata = new FileMetadata(TEST_HASH,FileType.PHENOTYPE_FILE,1L);
        ResponseEntity<String> response = restTemplate.postForEntity(REST_REPOSITORY_FILES,fileMetadata,String.class);
        Assert.assertEquals(HttpStatus.CREATED,response.getStatusCode());

    }

    @Test
    public void checkDoubleAddToRepository(){
        FileMetadata fileMetadata = new FileMetadata(TEST_HASH_REPEATED,FileType.PHENOTYPE_FILE,1L);
        ResponseEntity<String> response = restTemplate.postForEntity(REST_REPOSITORY_FILES,fileMetadata,String.class);
        Assert.assertEquals(HttpStatus.CREATED,response.getStatusCode());
        response = restTemplate.postForEntity(REST_REPOSITORY_FILES,fileMetadata,String.class);
        Assert.assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
    }
}
