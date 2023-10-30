package nl.fontys.sioux.siouxbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.exception.InvalidEmployeeException;
import nl.fontys.sioux.siouxbackend.business.interf.employee.*;
import nl.fontys.sioux.siouxbackend.domain.Employee;
import nl.fontys.sioux.siouxbackend.domain.request.employee.CreateEmployeeRequest;
import nl.fontys.sioux.siouxbackend.domain.request.employee.UpdateEmployeeRequest;
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
    private final UpdateEmployeeUseCase updateEmployeeUseCase;
    private final FilterEmployeesUseCase filterEmployeesUseCase;

    @PostMapping()
    public ResponseEntity<CreateEmployeeResponse> createEmployee(@RequestBody @Valid CreateEmployeeRequest request) {
        CreateEmployeeResponse response = createEmployeeUseCase.createEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable(value = "id") final Long id){
        Optional<Employee> employee = getEmployeeUseCase.getEmployee(id);
        if(employee.isEmpty()){
            throw new InvalidEmployeeException("USER_NOT_FOUND");
        }
        return ResponseEntity.ok().body(employee.get());
    }

    @GetMapping()
    public ResponseEntity<List<Employee>> getAllEmployees(){
        GetEmployeesResponse response = getEmployeesUseCase.getAllEmployees();
        return ResponseEntity.ok().body(response.getEmployees());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Employee>> filterEmployees(@RequestParam(name = "params") String parameters) {
        List<Employee> employees = filterEmployeesUseCase.filterEmployees(parameters);
        return ResponseEntity.ok().body(employees);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateEmployee(@PathVariable("id") long id, @RequestBody @Valid UpdateEmployeeRequest request){
        request.setId(id);

        updateEmployeeUseCase.updateEmployee(request);

        return ResponseEntity.noContent().build();
    }
}
