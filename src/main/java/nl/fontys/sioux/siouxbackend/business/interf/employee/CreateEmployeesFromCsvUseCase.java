package nl.fontys.sioux.siouxbackend.business.interf.employee;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface CreateEmployeesFromCsvUseCase {
    void readCsvData(Path path);
}
