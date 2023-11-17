package nl.fontys.sioux.siouxbackend.domain.response.appointment;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import nl.fontys.sioux.siouxbackend.domain.DTO.GetAppointmentEmployeeDTO;
import nl.fontys.sioux.siouxbackend.domain.Employee;
import nl.fontys.sioux.siouxbackend.repository.entity.EmployeeEntity;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class GetAppointmentResponse {
    private String clientName;

    private List<GetAppointmentEmployeeDTO> employees;

    private String clientEmail;

    private String clientPhoneNumber;

    private Date startTime;

    private Date endTime;

    private String location;

    private String description;

    private String licensePlate;
}
