package nl.fontys.sioux.siouxbackend.domain.request.appointment;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.fontys.sioux.siouxbackend.repository.entity.EmployeeEntity;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAppointmentRequest {
    @NotBlank
    private String clientName;

    private List<EmployeeEntity> employees;

    @NotBlank
    @Email
    private String clientEmail;

    @NotBlank
    private String clientPhoneNumber;

    @NotNull
    private Date startTime;

    @NotNull
    private Date endTime;

    private String location;

    private String description;

    private String licensePlate;
}
