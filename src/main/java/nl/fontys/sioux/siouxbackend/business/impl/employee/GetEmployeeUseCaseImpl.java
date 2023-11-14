package nl.fontys.sioux.siouxbackend.business.impl.employee;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.exception.InvalidEmployeeException;
import nl.fontys.sioux.siouxbackend.business.interf.employee.GetEmployeeUseCase;
import nl.fontys.sioux.siouxbackend.domain.Employee;
import nl.fontys.sioux.siouxbackend.repository.EmployeeRepository;
import nl.fontys.sioux.siouxbackend.repository.entity.EmployeeEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetEmployeeUseCaseImpl implements GetEmployeeUseCase {
    private final EmployeeRepository employeeRepository;

    @Transactional
    @Override
    public Optional<Employee> getEmployee(Long employeeID){
        if(!employeeRepository.existsById(employeeID)){
            throw new InvalidEmployeeException("USER_NOT_FOUND");
        }

        Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(employeeID);
        return employeeEntity.map(EmployeeConverter::convert);
    }
}
