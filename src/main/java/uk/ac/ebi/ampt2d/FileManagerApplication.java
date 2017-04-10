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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import uk.ac.ebi.ampt2d.storage.StorageProperties;

/**
 * Register Springs JSR310 JPA converters for mapping of LocalDateTime in postgresql.
 *
 * See <a href="https://spring.io/blog/2015/03/26/what-s-new-in-spring-data-fowler#jsr-310-and-threeten-backport-support">spring.io blog</a>
 * See <a href="http://stackoverflow.com/questions/29517508/how-to-persist-jsr-310-types-with-spring-data-jpa/29542062#29542062"> stackoverflow QA</a>
 */
@EntityScan(
        basePackageClasses = {FileManagerApplication.class, Jsr310JpaConverters.class}
)
@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class FileManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileManagerApplication.class, args);
    }
}
