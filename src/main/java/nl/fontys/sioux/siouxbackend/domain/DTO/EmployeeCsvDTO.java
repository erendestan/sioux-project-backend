package nl.fontys.sioux.siouxbackend.domain.DTO;

import com.opencsv.bean.CsvBindByName;
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
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCsvDTO {
    @CsvBindByName(column = "first_name")
    @NotBlank
    @Length(min = 2, max = 50)
    private String firstName;

    @CsvBindByName(column = "last_name")
    @NotBlank
    @Length(min = 2, max = 50)
    private String lastName;

    @CsvBindByName(column = "email")
    @NotBlank
    @Email
    @Length(min = 2, max = 50)
    private String email;

    @CsvBindByName(column = "position")
    @NotNull
    private Position position;
}
