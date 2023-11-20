package nl.fontys.sioux.siouxbackend.domain.request.appointment;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAppointmentRequest {
    private Long appointmentID;

    private String clientName;

    private List<Long> employeeIDs;

    @Email
    private String clientEmail;

    private String clientPhoneNumber;

    private Date startTime;

    private Date endTime;

    private String location;

    private String description;

    private String licensePlate;
}
