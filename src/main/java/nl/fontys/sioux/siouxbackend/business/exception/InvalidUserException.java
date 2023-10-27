package nl.fontys.sioux.siouxbackend.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidUserException extends ResponseStatusException {
    public InvalidUserException() {
        super(HttpStatus.BAD_REQUEST, "COUNTRY_INVALID");
    }

    public InvalidUserException(String errorCode) {
        super(HttpStatus.BAD_REQUEST, errorCode);
    }
}
