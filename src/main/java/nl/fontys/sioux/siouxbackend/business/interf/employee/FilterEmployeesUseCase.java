package nl.fontys.sioux.siouxbackend.business.interf.employee;

import nl.fontys.sioux.siouxbackend.domain.Employee;

import java.util.List;

public interface FilterEmployeesUseCase {
    List<Employee> filterEmployees(String parameters);
}
