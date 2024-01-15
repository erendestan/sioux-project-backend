package nl.fontys.sioux.siouxbackend.business.impl.employee;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.exception.InvalidEmployeeException;
import nl.fontys.sioux.siouxbackend.business.interf.employee.CreateEmployeeUseCase;
import nl.fontys.sioux.siouxbackend.business.interf.employee.SendNewEmployeeEmailUseCase;
import nl.fontys.sioux.siouxbackend.domain.request.employee.CreateEmployeeRequest;
import nl.fontys.sioux.siouxbackend.domain.response.employee.CreateEmployeeResponse;
import nl.fontys.sioux.siouxbackend.repository.EmployeeRepository;
import nl.fontys.sioux.siouxbackend.repository.entity.EmployeeEntity;
import org.apache.commons.lang3.text.WordUtils;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateEmployeeUseCaseImpl implements CreateEmployeeUseCase {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final SendNewEmployeeEmailUseCase sendNewEmployeeEmailUseCase;

    @Transactional
    @Override
    public CreateEmployeeResponse createEmployee(CreateEmployeeRequest request){
        if(employeeRepository.existsByEmail(request.getEmail())){
            throw new InvalidEmployeeException("EMAIL_DUPLICATED");
        }

        PasswordGenerator generator = new PasswordGenerator();
        CharacterRule lowerChars = new CharacterRule(EnglishCharacterData.LowerCase);
        CharacterRule upperChars = new CharacterRule(EnglishCharacterData.UpperCase);
        CharacterRule digitChars = new CharacterRule(EnglishCharacterData.Digit);

        String generatedPassword = generator.generatePassword(12, lowerChars, upperChars, digitChars);
        String encodedPassword = passwordEncoder.encode(generatedPassword);

        EmployeeEntity newEmployee = EmployeeEntity.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(encodedPassword)
                .position(request.getPosition())
                .active(true)
                .build();

        EmployeeEntity savedEmployee = employeeRepository.save(newEmployee);
        sendEmployeeEmail(savedEmployee, generatedPassword);
        return CreateEmployeeResponse.builder()
                .employeeID(savedEmployee.getId())
                .build();
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
