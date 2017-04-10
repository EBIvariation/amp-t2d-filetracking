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

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uk.ac.ebi.ampt2d.FileType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "file_metadata")
public class FileMetadata {

    private static final int MIN_FILE_HASH = 1;

    private static final int MAX_FILE_HASH = 128;

    @Id
    @GeneratedValue
    private long id;

    @Column(length = MAX_FILE_HASH, unique = true)
    @Size(min = MIN_FILE_HASH, max = MAX_FILE_HASH)
    private String hash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileType fileType;

    private long size;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fileMetadata", fetch = FetchType.EAGER)
    private final Set<SourceFilePath> sourceFilePaths;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime lastModifiedDate;

    public FileMetadata(String hash, FileType fileType, long size) {
        this();
        this.hash = hash;
        this.fileType = fileType;
        this.size = size;
    }

    private FileMetadata() {
        sourceFilePaths = new LinkedHashSet<>();
    }

    public String getHash() {
        return hash;
    }

    public Set<SourceFilePath> getSourceFilePaths() {
        return Collections.unmodifiableSet(sourceFilePaths);
    }

    public void addSourceFilePaths(SourceFilePath... sourceFilePaths) {
        Stream.of(sourceFilePaths).filter(Objects::nonNull).forEach(this::addSourceFile);
    }

    private void addSourceFile(SourceFilePath sourceFilePath) {
        sourceFilePath.setFileMetadata(this);
        sourceFilePaths.add(sourceFilePath);
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

}
