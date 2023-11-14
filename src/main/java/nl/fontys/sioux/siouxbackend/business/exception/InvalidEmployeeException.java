package nl.fontys.sioux.siouxbackend.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidEmployeeException extends ResponseStatusException {
    public InvalidEmployeeException() {
        super(HttpStatus.BAD_REQUEST, "EMPLOYEE_INVALID");
    }

    public InvalidEmployeeException(String errorCode) {
        super(HttpStatus.BAD_REQUEST, errorCode);
    }
}
