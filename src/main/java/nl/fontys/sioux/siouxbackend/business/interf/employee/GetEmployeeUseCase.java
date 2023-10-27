package nl.fontys.sioux.siouxbackend.business.interf.employee;

import nl.fontys.sioux.siouxbackend.domain.Employee;

import java.util.Optional;

public interface GetEmployeeUseCase {
    Optional<Employee> getEmployee(Long employeeID);
}
