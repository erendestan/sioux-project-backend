package nl.fontys.sioux.siouxbackend.domain.request.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.sioux.siouxbackend.domain.Position;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeRequest {
    private Long id;
    @Length(min = 2, max = 50)
    private String firstName;

    @Length(min = 2, max = 50)
    private String lastName;

    @Email
    @Length(min = 2, max = 50)
    private String email;

    @Length(min = 2)
    private String password;

    private Position position;

    private Boolean active;
}
