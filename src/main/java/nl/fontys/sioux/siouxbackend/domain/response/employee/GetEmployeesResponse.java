package nl.fontys.sioux.siouxbackend.domain.response.employee;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.sioux.siouxbackend.domain.Employee;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetEmployeesResponse {
    private List<Employee> employees;
}
