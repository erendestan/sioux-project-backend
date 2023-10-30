package nl.fontys.sioux.siouxbackend.business.impl.employee;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.interf.employee.FilterEmployeesUseCase;
import nl.fontys.sioux.siouxbackend.domain.Employee;
import nl.fontys.sioux.siouxbackend.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FilterEmployeesUseCaseImpl implements FilterEmployeesUseCase {
    private final EmployeeRepository employeeRepository;

    @Transactional
    @Override
    public List<Employee> filterEmployees(String params) {
        return employeeRepository.filterEmployees(params).stream().map(EmployeeConverter::convert).toList();
    }
}
