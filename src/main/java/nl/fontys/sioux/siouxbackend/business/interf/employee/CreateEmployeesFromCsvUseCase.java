package nl.fontys.sioux.siouxbackend.business.interf.employee;

import nl.fontys.sioux.siouxbackend.domain.response.employee.CreateEmployeesFromCsvResponse;

import java.nio.file.Path;

public interface CreateEmployeesFromCsvUseCase {
    CreateEmployeesFromCsvResponse readCsvData(Path path);
}
