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

@RestControllerAdvice
public class ExceptionHandling {

    private final Logger logger = LoggerFactory.getLogger(ExceptionHandling.class);

    @ExceptionHandler({IOException.class, StorageException.class})
    public ResponseEntity<ExceptionError> exceptionHandler(HttpServletRequest request, Exception exception) {
        logger.error("Exception on rest call " + request.getRequestURI(), exception);
        return new ExceptionErrorResponseEntity("Internal server error", request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
