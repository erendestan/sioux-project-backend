package nl.fontys.sioux.siouxbackend.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmployeeNotFoundException extends ResponseStatusException {
    public EmployeeNotFoundException(String email) {
        super(HttpStatus.NOT_FOUND, "Employee not found by email: " + email);
    }
}
