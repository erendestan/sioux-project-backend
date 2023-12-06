package nl.fontys.sioux.siouxbackend.business.employee;

import nl.fontys.sioux.siouxbackend.business.interf.employee.CreateEmployeesFromCsvUseCase;
import nl.fontys.sioux.siouxbackend.domain.Position;
import nl.fontys.sioux.siouxbackend.repository.EmployeeRepository;
import nl.fontys.sioux.siouxbackend.repository.entity.EmployeeEntity;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class CreateEmployeesFromCsvTest {
//    @Test
//    void readCsvDataTest_ShouldSaveEmployees(){
//        CreateEmployeesFromCsvUseCase createEmployeesFromCsvUseCase;
//
//        ClassPathResource resource = new ClassPathResource("employees.csv");
//        Path tempFilePath = Files.createTempFile("temp-test", ".csv");
//        Files.copy(resource.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);
//
//        EmployeeEntity expectedEmployeeOne = EmployeeEntity.builder()
//                .firstName("Kinara")
//                .lastName("Safina")
//                .email("kinara@sioux.com")
//                .password("pass")
//                .position(Position.Admin)
//                .active(true)
//                .build();
//
//        EmployeeEntity expectedEmployeeTwo = EmployeeEntity.builder()
//                .firstName("Rafael")
//                .lastName("Cocco Belliard")
//                .email("rafa@sioux.com")
//                .password("password")
//                .position(Position.Secretary)
//                .active(true)
//                .build();
//
//        createEmployeesFromCsvUseCase.readCsvData(tempFilePath);
//
//
//
//    }
}
