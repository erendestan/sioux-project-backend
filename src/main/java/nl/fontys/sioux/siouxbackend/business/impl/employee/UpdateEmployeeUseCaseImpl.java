package nl.fontys.sioux.siouxbackend.business.impl.employee;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.exception.EmailAlreadyExistsException;
import nl.fontys.sioux.siouxbackend.business.exception.InvalidEmployeeException;
import nl.fontys.sioux.siouxbackend.business.interf.employee.UpdateEmployeeUseCase;
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

        EmployeeEntity foundEmployee = employeeOptional.get();

        if(!request.getEmail().equals(foundEmployee.getEmail())){
            if(employeeRepository.existsByEmail(request.getEmail())){
                throw new EmailAlreadyExistsException();
            }
        }
        foundEmployee.setEmail(request.getEmail());

        if(request.getActive() != null){ foundEmployee.setActive(request.getActive()); }

        if(request.getPassword() != null){ foundEmployee.setPassword(request.getPassword()); }

        if(request.getPosition() != null){ foundEmployee.setPosition(request.getPosition()); }

        if(request.getFirstName() != null){ foundEmployee.setFirstName(request.getFirstName()); }

        if(request.getLastName() != null){ foundEmployee.setLastName(request.getLastName()); }

        employeeRepository.save(foundEmployee);

    }
}
