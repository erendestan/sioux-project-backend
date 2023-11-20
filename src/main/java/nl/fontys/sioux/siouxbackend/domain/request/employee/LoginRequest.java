package nl.fontys.sioux.siouxbackend.domain.request.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank
    @Email
    @Length(min = 2, max = 50)
    private String email;

    @NotBlank
    @Length(min = 2)
    private String password;
}
