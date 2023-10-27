package nl.fontys.sioux.siouxbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.exception.InvalidUserException;
import nl.fontys.sioux.siouxbackend.business.interf.employee.CreateEmployeeUseCase;
import nl.fontys.sioux.siouxbackend.business.interf.employee.GetEmployeeUseCase;
import nl.fontys.sioux.siouxbackend.business.interf.employee.GetEmployeesUseCase;
import nl.fontys.sioux.siouxbackend.domain.Employee;
import nl.fontys.sioux.siouxbackend.domain.request.employee.CreateEmployeeRequest;
import nl.fontys.sioux.siouxbackend.domain.response.employee.CreateEmployeeResponse;
import nl.fontys.sioux.siouxbackend.domain.response.employee.GetEmployeesResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
@CrossOrigin
public class EmployeeController {
    private final CreateEmployeeUseCase createEmployeeUseCase;
    private final GetEmployeesUseCase getEmployeesUseCase;
    private final GetEmployeeUseCase getEmployeeUseCase;

    @PostMapping()
    public ResponseEntity<CreateEmployeeResponse> createEmployee(@RequestBody @Valid CreateEmployeeRequest request) {
        CreateEmployeeResponse response = createEmployeeUseCase.createEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable(value = "id") final Long id){
        Optional<Employee> employee = getEmployeeUseCase.getEmployee(id);
        if(employee.isEmpty()){
            throw new InvalidUserException("USER_NOT_FOUND");
        }
        return ResponseEntity.ok().body(employee.get());
    }

    @GetMapping()
    public ResponseEntity<List<Employee>> getAllEmployees(){
        GetEmployeesResponse response = getEmployeesUseCase.getAllEmployees();
        return ResponseEntity.ok().body(response.getEmployees());
    }
}
