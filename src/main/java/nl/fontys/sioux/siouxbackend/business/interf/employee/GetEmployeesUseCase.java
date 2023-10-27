package nl.fontys.sioux.siouxbackend.business.interf.employee;

import nl.fontys.sioux.siouxbackend.domain.response.employee.GetEmployeesResponse;

public interface GetEmployeesUseCase {
    GetEmployeesResponse getAllEmployees();
}
