package nl.fontys.sioux.siouxbackend.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ImportCSVException extends ResponseStatusException {
    public ImportCSVException(String errorCode) { super(HttpStatus.BAD_REQUEST, errorCode); }
}
