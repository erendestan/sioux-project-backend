package nl.fontys.sioux.siouxbackend.business.impl.employee;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.interf.employee.DeleteEmployeeUseCase;
import nl.fontys.sioux.siouxbackend.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteEmployeeUseCaseImpl implements DeleteEmployeeUseCase {
    private final EmployeeRepository employeeRepository;
    @Override
    @Transactional
    public void deleteEmployee(long employeeID) {
        employeeRepository.deleteById(employeeID);
    }
}
