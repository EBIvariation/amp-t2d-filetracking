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
package uk.ac.ebi.ampt2d.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uk.ac.ebi.ampt2d.rest.responses.ExceptionError;
import uk.ac.ebi.ampt2d.rest.responses.ExceptionErrorResponseEntity;
import uk.ac.ebi.ampt2d.storage.exceptions.StorageException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * This Class registers itself as a manager for exceptions thrown in the REST services.
 */
@RestControllerAdvice
public class ExceptionHandling {

    private final Logger logger = LoggerFactory.getLogger(ExceptionHandling.class);

    @ExceptionHandler({IOException.class, StorageException.class})
    public ResponseEntity<ExceptionError> exceptionHandler(HttpServletRequest request, Exception exception) {
        logger.error("Exception on rest call " + request.getRequestURI(), exception);
        return new ExceptionErrorResponseEntity("Internal server error", request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
