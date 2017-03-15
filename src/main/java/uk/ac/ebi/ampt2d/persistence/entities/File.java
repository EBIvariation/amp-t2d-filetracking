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
package uk.ac.ebi.ampt2d.persistence.entities;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uk.ac.ebi.ampt2d.FileType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class File {

    public static final int MIN_FILE_HASH = 1;
    public static final int MAX_FILE_HASH = 128;

    @Id
    @Column(length = MAX_FILE_HASH)
    @Size(min = MIN_FILE_HASH, max = MAX_FILE_HASH)
    private String hash;

    private FileType type;

    private Integer size;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "file", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SourceFilePath> sourceFilePaths;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime lastModifiedDate;

    public File(String hash,
                FileType type,
                Integer size,
                List<SourceFilePath> sourceFilePaths) {
        this.hash = hash;
        this.type = type;
        this.size = size;
        this.sourceFilePaths = sourceFilePaths;
    }

    public File(java.io.File file, FileType type, List<SourceFilePath> sourceFilePaths) {
        try {
            this.hash = Files.hash(file, Hashing.sha384()).toString();
        } catch ( IOException e) {
            e.printStackTrace();
        }
        this.type = type;
        this.size = (int) file.length();
        this.sourceFilePaths = sourceFilePaths;
    }

    public File() {
    }

}
