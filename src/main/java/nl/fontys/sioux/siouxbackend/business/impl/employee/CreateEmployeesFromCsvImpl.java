package nl.fontys.sioux.siouxbackend.business.impl.employee;

import com.opencsv.CSVReader;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.exception.ImportCSVException;
import nl.fontys.sioux.siouxbackend.business.interf.employee.CreateEmployeesFromCsvUseCase;
import nl.fontys.sioux.siouxbackend.domain.Position;
import nl.fontys.sioux.siouxbackend.domain.response.employee.CreateEmployeesFromCsvResponse;
import nl.fontys.sioux.siouxbackend.repository.EmployeeRepository;
import nl.fontys.sioux.siouxbackend.repository.entity.EmployeeEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

@AllArgsConstructor
@Service
public class CreateEmployeesFromCsvImpl implements CreateEmployeesFromCsvUseCase {
    private final EmployeeRepository employeeRepository;
    @Transactional
    @Override
    public CreateEmployeesFromCsvResponse readCsvData(Path path) {
        Long count = 0L;
        try(Reader reader = Files.newBufferedReader(path)) {
            try (CSVReader csvReader = new CSVReader(reader)) {

                String[] headers = csvReader.readNext();
                String[] headerNames = {"first_name", "last_name", "email", "password", "position", "active"};
                if(headers == null){
                    throw new IOException("FILE_EMPTY");
                }

                if(headers.length != headerNames.length){
                    throw new IOException("INCOMPLETE_DATA");
                }

                for(int i = 0 ; i < headerNames.length ; i ++) {
                    if(!headers[i].equals(headerNames[i])) {
                        throw new IOException("INVALID_DATA_FORMAT");
                    }
                }

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

                    if(!employeeRepository.existsByEmail(newEmployee.getEmail())){
                        employeeRepository.save(newEmployee);
                        count++;
                    }
                }
            }
        }
        catch (IOException e) {
            throw new ImportCSVException(e.getMessage());
        }
        return CreateEmployeesFromCsvResponse.builder()
                .count(count)
                .build();
    }
}
