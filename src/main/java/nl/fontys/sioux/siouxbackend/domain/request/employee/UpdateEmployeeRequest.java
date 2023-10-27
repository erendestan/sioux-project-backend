package nl.fontys.sioux.siouxbackend.domain.request.employee;

import nl.fontys.sioux.siouxbackend.domain.Position;

public class UpdateEmployeeRequest {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Position position;
    private Boolean active;
}
