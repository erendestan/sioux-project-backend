package nl.fontys.sioux.siouxbackend.business.impl.employee;

import nl.fontys.sioux.siouxbackend.domain.Employee;
import nl.fontys.sioux.siouxbackend.repository.entity.EmployeeEntity;

public class EmployeeConverter {
    public static Employee convert(EmployeeEntity employee){
        return Employee.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .password(employee.getPassword())
                .position(employee.getPosition())
                .active(employee.getActive())
                .build();
    }
}
