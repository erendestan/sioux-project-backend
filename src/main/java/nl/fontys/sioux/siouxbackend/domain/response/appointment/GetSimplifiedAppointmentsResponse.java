package nl.fontys.sioux.siouxbackend.domain.response.appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import nl.fontys.sioux.siouxbackend.domain.DTO.GetAppointmentEmployeeDTO;
import nl.fontys.sioux.siouxbackend.domain.Employee;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class GetSimplifiedAppointmentsResponse {
    private Long id;
    private String clientName;
    private Date startTime;
    private Date endTime;
    private List<GetAppointmentEmployeeDTO> employees;
}
