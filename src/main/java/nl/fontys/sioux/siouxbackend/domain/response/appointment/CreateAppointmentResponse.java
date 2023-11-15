package nl.fontys.sioux.siouxbackend.domain.response.appointment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateAppointmentResponse {
    private Long appointmentID;
}
