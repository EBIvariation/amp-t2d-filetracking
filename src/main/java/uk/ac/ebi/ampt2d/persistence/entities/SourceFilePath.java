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

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Persistence information required to recover a stored file.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
public class SourceFilePath {

    private static final int MIN_PATH_LENGTH = 1;
    private static final int MAX_PATH_LENGTH = 1024;

    @Id
    @GeneratedValue
    private long id;

    @JsonBackReference
    @JoinColumn(name = "file_id")
    @ManyToOne(cascade = CascadeType.ALL)
    private FileMetadata fileMetadata;

    @Column(length = MAX_PATH_LENGTH, nullable = false)
    @Size(min = MIN_PATH_LENGTH, max = MAX_PATH_LENGTH)
    @NotNull
    private String path;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    public SourceFilePath(String path) {
        this.path = path;
    }

    protected SourceFilePath() {
    }

    public FileMetadata getFileMetadata() {
        return fileMetadata;
    }

    void setFileMetadata(FileMetadata fileMetadata) {
        this.fileMetadata = fileMetadata;
    }

    public String getPath() {
        return path;
    }

}
