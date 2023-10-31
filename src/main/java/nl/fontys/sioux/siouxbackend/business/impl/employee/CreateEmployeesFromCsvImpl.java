package nl.fontys.sioux.siouxbackend.business.impl.employee;

import com.opencsv.CSVReader;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.exception.InvalidEmployeeException;
import nl.fontys.sioux.siouxbackend.business.interf.employee.CreateEmployeesFromCsvUseCase;
import nl.fontys.sioux.siouxbackend.domain.Position;
import nl.fontys.sioux.siouxbackend.repository.EmployeeRepository;
import nl.fontys.sioux.siouxbackend.repository.entity.EmployeeEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

@AllArgsConstructor
@Service
public class CreateEmployeesFromCsvImpl implements CreateEmployeesFromCsvUseCase {
    private final EmployeeRepository employeeRepository;
    @Override
    public void readCsvData(Path path) {
        try(Reader reader = Files.newBufferedReader(path)) {
            try (CSVReader csvReader = new CSVReader(reader)) {

                csvReader.readNext();

                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    String positionCsv = line[4].trim();
                    Position position = Position.valueOf(positionCsv);

                    EmployeeEntity newEmployee = EmployeeEntity
                            .builder()
                            .firstName(line[0])
                            .lastName(line[1])
                            .email(line[2])
                            .password(line[3])
                            .position(position)
                            .active(Boolean.parseBoolean(line[5]))
                            .build();

                    if(employeeRepository.existsByEmail(newEmployee.getEmail())){
                        throw new InvalidEmployeeException("EMAIL_DUPLICATED");
                    }

                    employeeRepository.save(newEmployee);
                }
            }
        }
        catch (IOException e) {
            // do sth
        }

    }
}
