package nl.fontys.sioux.siouxbackend.business.impl.employee;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.interf.employee.GetEmployeesUseCase;
import nl.fontys.sioux.siouxbackend.domain.Employee;
import nl.fontys.sioux.siouxbackend.domain.response.employee.GetEmployeesResponse;
import nl.fontys.sioux.siouxbackend.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetEmployeesUseCaseImpl implements GetEmployeesUseCase {
    private final EmployeeRepository employeeRepository;

    @Transactional
    @Override
    public GetEmployeesResponse getAllEmployees(){
        List<Employee> employees = employeeRepository.findAll()
                .stream()
                .map(EmployeeConverter::convert)
                .toList();

        return GetEmployeesResponse.builder()
                .employees(employees)
                .build();
    }
}
