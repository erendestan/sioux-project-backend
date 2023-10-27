package nl.fontys.sioux.siouxbackend.business.interf.employee;

import nl.fontys.sioux.siouxbackend.domain.request.employee.CreateEmployeeRequest;
import nl.fontys.sioux.siouxbackend.domain.response.employee.CreateEmployeeResponse;

public interface CreateEmployeeUseCase {
    CreateEmployeeResponse createEmployee(CreateEmployeeRequest request);
}
