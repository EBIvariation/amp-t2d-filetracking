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
package uk.ac.ebi.ampt2d.rest.responses;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;

/**
 * Custom class to avoid leaking internal exception to the rest output message. {@link JsonView} annotation is added
 * to the class members to avoid creating a converter to return it as a error response when a exception has occurred.
 * Spring management of exceptions is outside of the control that provides automatic json converters.
 */
public class ExceptionError {

    @JsonView
    private final long timestamp;

    @JsonView
    private final int status;

    @JsonView
    private final String error;

    @JsonView
    private final String message;

    @JsonView
    private final String path;

    ExceptionError(long timestamp, HttpStatus status, String message, String path) {
        this.timestamp = timestamp;
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.path = path;
    }

}
