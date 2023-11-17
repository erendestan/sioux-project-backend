package nl.fontys.sioux.siouxbackend.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GetAppointmentEmployeeDTO {
    private Long id;
    private String firstName;
    private String lastName;
}
