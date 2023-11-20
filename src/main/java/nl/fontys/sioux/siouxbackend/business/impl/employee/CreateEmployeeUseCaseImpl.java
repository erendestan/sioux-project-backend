package nl.fontys.sioux.siouxbackend.business.impl.employee;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.exception.InvalidEmployeeException;
import nl.fontys.sioux.siouxbackend.business.interf.employee.CreateEmployeeUseCase;
import nl.fontys.sioux.siouxbackend.domain.request.employee.CreateEmployeeRequest;
import nl.fontys.sioux.siouxbackend.domain.response.employee.CreateEmployeeResponse;
import nl.fontys.sioux.siouxbackend.repository.EmployeeRepository;
import nl.fontys.sioux.siouxbackend.repository.entity.EmployeeEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateEmployeeUseCaseImpl implements CreateEmployeeUseCase {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public CreateEmployeeResponse createEmployee(CreateEmployeeRequest request){
        if(employeeRepository.existsByEmail(request.getEmail())){
            throw new InvalidEmployeeException("EMAIL_DUPLICATED");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        EmployeeEntity newEmployee = EmployeeEntity.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(encodedPassword)
                .position(request.getPosition())
                .active(true)
                .build();

        EmployeeEntity savedEmployee = employeeRepository.save(newEmployee);
        return CreateEmployeeResponse.builder()
                .employeeID(savedEmployee.getId())
                .build();
    }
}
