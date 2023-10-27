package nl.fontys.sioux.siouxbackend.business.interf.employee;

import nl.fontys.sioux.siouxbackend.domain.request.employee.UpdateEmployeeRequest;

public interface UpdateEmployeeUseCase {
    void updateEmployee(UpdateEmployeeRequest request);
}
