package uk.ac.ebi.ampt2d.rest.responses;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class ExceptionErrorResponseEntity extends ResponseEntity<ExceptionError> {

    public ExceptionErrorResponseEntity(String message, HttpServletRequest request,
                                        MultiValueMap<String, String> headers, HttpStatus status) {
        super(new ExceptionError(new Date().getTime(), status, message, request.getRequestURI()), headers, status);
    }

    public ExceptionErrorResponseEntity(String message, HttpServletRequest request, HttpStatus status) {
        super(new ExceptionError(new Date().getTime(), status, message, request.getRequestURI()), new HttpHeaders(),
                status);
    }
}
