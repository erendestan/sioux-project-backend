package nl.fontys.sioux.siouxbackend.business.impl.employee;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.exception.EmailAlreadyExistsException;
import nl.fontys.sioux.siouxbackend.business.exception.InvalidEmployeeException;
import nl.fontys.sioux.siouxbackend.business.interf.employee.UpdateEmployeeUseCase;
import nl.fontys.sioux.siouxbackend.domain.Employee;
import nl.fontys.sioux.siouxbackend.domain.request.employee.UpdateEmployeeRequest;
import nl.fontys.sioux.siouxbackend.repository.EmployeeRepository;
import nl.fontys.sioux.siouxbackend.repository.entity.EmployeeEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateEmployeeUseCaseImpl implements UpdateEmployeeUseCase {
    private final EmployeeRepository employeeRepository;
    @Override
    @Transactional
    public void updateEmployee(UpdateEmployeeRequest request) {
        Optional<EmployeeEntity> employeeOptional = employeeRepository.findById(request.getId());

        if(employeeOptional.isEmpty()){
            throw new InvalidEmployeeException("EMPLOYEE_ID_INVALID");
        }

        if(employeeRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException();
        }

        EmployeeEntity foundEmployee = employeeOptional.get();

        if(!request.getEmail().equals(foundEmployee.getEmail())){
            foundEmployee.setEmail(request.getEmail());
        }

        if(!request.getActive().equals(foundEmployee.getActive())){
            foundEmployee.setActive(request.getActive());
        }

        if(!request.getPassword().equals(foundEmployee.getPassword())){
            foundEmployee.setPassword(request.getPassword());
        }

        if(!request.getPosition().equals(foundEmployee.getPosition())){
            foundEmployee.setPosition(request.getPosition());
        }

        if(!request.getFirstName().equals(foundEmployee.getFirstName())){
            foundEmployee.setFirstName(request.getFirstName());
        }

        if(!request.getLastName().equals(foundEmployee.getLastName())){
            foundEmployee.setLastName(request.getLastName());
        }

        employeeRepository.save(foundEmployee);

    }
}
