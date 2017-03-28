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

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.Assert;
import uk.ac.ebi.ampt2d.Type;

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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="file")
public class FileMetadata {
    public static final int MIN_FILE_HASH = 1;

    public static final int MAX_FILE_HASH = 128;

    @Id
    @GeneratedValue
    private long id;

    @Column(length = MAX_FILE_HASH, unique = true)
    @Size(min = MIN_FILE_HASH, max = MAX_FILE_HASH)
    private String hash;

    @Enumerated(EnumType.STRING)
    private Type type;

    private String name;

    private long size;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fileMetadata", fetch = FetchType.EAGER)
    private Set<SourceFilePath> sourceFilePaths;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime lastModifiedDate;

    public FileMetadata(String hash, String name, Type type, long size) {
        this.hash = hash;
        this.name = name;
        this.type = type;
        this.size = size;
    }

    public FileMetadata(File file, Type type, String name) throws IOException {
        this.hash = Files.hash(file, Hashing.sha384()).toString();
        this.type = type;
        this.size = file.length();
        this.name = name;
    }

    public FileMetadata() {
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        Assert.hasText(hash, "Hash is required");
        this.hash = hash;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Assert.hasText(name, "File name is required");
        this.name = name;
    }

    public Set<SourceFilePath> getSourceFilePaths() {
        return sourceFilePaths;
    }

    public void setSourceFilePaths(Set<SourceFilePath> sourceFilePaths) {
        if (sourceFilePaths != null) {
            for (SourceFilePath sourceFilePath : sourceFilePaths) {
                sourceFilePath.setFileMetadata(this);
            }
            this.sourceFilePaths = sourceFilePaths;
        }
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
