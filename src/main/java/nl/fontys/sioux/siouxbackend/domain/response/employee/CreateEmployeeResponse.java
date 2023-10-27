package nl.fontys.sioux.siouxbackend.domain.response.employee;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateEmployeeResponse {
    private Long employeeID;
}
