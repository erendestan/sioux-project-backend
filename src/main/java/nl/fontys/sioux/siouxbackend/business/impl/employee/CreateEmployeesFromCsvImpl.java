package nl.fontys.sioux.siouxbackend.business.impl.employee;

import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.interf.employee.CreateEmployeesFromCsvUseCase;
import nl.fontys.sioux.siouxbackend.business.interf.employee.SendNewEmployeeEmailUseCase;
import nl.fontys.sioux.siouxbackend.domain.DTO.EmployeeCsvDTO;
import nl.fontys.sioux.siouxbackend.domain.response.employee.CreateEmployeesFromCsvResponse;
import nl.fontys.sioux.siouxbackend.repository.EmployeeRepository;
import nl.fontys.sioux.siouxbackend.repository.entity.EmployeeEntity;
import org.apache.commons.lang3.text.WordUtils;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.List;

@AllArgsConstructor
@Service
public class CreateEmployeesFromCsvImpl implements CreateEmployeesFromCsvUseCase {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private SendNewEmployeeEmailUseCase sendNewEmployeeEmailUseCase;

    @Transactional
    @Override
    public CreateEmployeesFromCsvResponse readCsvData(Path csvFilePath) {
        PasswordGenerator generator = new PasswordGenerator();
        CharacterRule lowerChars = new CharacterRule(EnglishCharacterData.LowerCase);
        CharacterRule upperChars = new CharacterRule(EnglishCharacterData.UpperCase);
        CharacterRule digitChars = new CharacterRule(EnglishCharacterData.Digit);

        try {
            List<EmployeeCsvDTO> employeeCsvDTOList = new CsvToBeanBuilder<EmployeeCsvDTO>(new FileReader(csvFilePath.toString()))
                    .withType(EmployeeCsvDTO.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();

            List<EmployeeEntity> employeeEntities = employeeCsvDTOList.stream()
                    .map(dto -> EmployeeEntity.builder()
                            .firstName(dto.getFirstName())
                            .lastName(dto.getLastName())
                            .password("notpasswordyet")
                            .active(true)
                            .build())
                    .toList();

            for (EmployeeEntity employee: employeeEntities) {
                String generatedPassword = generator.generatePassword(12, lowerChars, upperChars, digitChars);
                employee.setPassword(passwordEncoder.encode(generatedPassword));
                employeeRepository.save(employee);
                sendEmployeeEmail(employee, generatedPassword);
            }
            return CreateEmployeesFromCsvResponse.builder()
                    .count((long) employeeEntities.size())
                    .build();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendEmployeeEmail(EmployeeEntity employee, String generatedPassword){
        String subject = "Welcome to Sioux!";
        String htmlTemplate = """
                <html>
                    <body>
                        <h1>Sioux Account Created Confirmation</h1>
                        <p>Dear %s, your new Sioux %s account has been created.</p>
                                
                        <h3>How to access your Sioux account:</h3>
                        <p>Email: <b>%s</b></p>
                        <p>Password: <b>%s</b></p>
                    </body>
                </html>
                """;
        String emailBody = String.format(htmlTemplate,
                (employee.getFirstName() + " " + employee.getLastName()),
                WordUtils.uncapitalize(String.valueOf(employee.getPosition())),
                employee.getEmail(),
                generatedPassword);
        sendNewEmployeeEmailUseCase.sendEmployeeCreatedConfirmation(employee.getEmail(), subject, emailBody);
    }
}