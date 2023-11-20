package nl.fontys.sioux.siouxbackend.business.impl.employee;

import lombok.AllArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.exception.EmployeeNotFoundException;
import nl.fontys.sioux.siouxbackend.business.exception.InvalidCredentialsException;
import nl.fontys.sioux.siouxbackend.business.interf.employee.LoginUseCase;
import nl.fontys.sioux.siouxbackend.domain.request.employee.LoginRequest;
import nl.fontys.sioux.siouxbackend.domain.response.employee.AccessTokenResponse;
import nl.fontys.sioux.siouxbackend.repository.EmployeeRepository;
import nl.fontys.sioux.siouxbackend.repository.entity.EmployeeEntity;
import nl.fontys.sioux.siouxbackend.security.token.AccessToken;
import nl.fontys.sioux.siouxbackend.security.token.AccessTokenSerializer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class LoginUseCaseImpl implements LoginUseCase {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenSerializer accessTokenSerializer;

    public AccessTokenResponse login(LoginRequest request) {
        Optional<EmployeeEntity> employeeOptional = employeeRepository.findByEmail(request.getEmail());

        if (employeeOptional.isEmpty()) {
            throw new EmployeeNotFoundException(request.getEmail());
        }

        EmployeeEntity employee = employeeOptional.get();

        if (!passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
            throw new InvalidCredentialsException();
        }

        AccessToken accessToken = AccessToken.builder()
                .subject(employee.getEmail())
                .userId(employee.getId())
                .role(employee.getPosition())
                .build();

        return new AccessTokenResponse(accessTokenSerializer.encode(accessToken));
    }
}
