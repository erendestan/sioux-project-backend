package nl.fontys.sioux.siouxbackend.business.impl.appointment;

import lombok.AllArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.impl.employee.EmployeeConverter;
import nl.fontys.sioux.siouxbackend.business.interf.appointment.GetAppointmentUseCase;
import nl.fontys.sioux.siouxbackend.domain.Employee;
import nl.fontys.sioux.siouxbackend.domain.response.appointment.GetAppointmentResponse;
import nl.fontys.sioux.siouxbackend.repository.AppointmentRepository;
import nl.fontys.sioux.siouxbackend.repository.entity.AppointmentEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GetAppointmentUseCaseImpl implements GetAppointmentUseCase {
    private final AppointmentRepository appointmentRepository;
    @Override
    public GetAppointmentResponse getAppointment(Long id) {
        if(!appointmentRepository.existsById(id)){
            // throw exception
        }

        Optional<AppointmentEntity> optionalAppointment = appointmentRepository.findById(id);

        if(optionalAppointment.isEmpty()){
            // throw exception
        }

        AppointmentEntity appointment = optionalAppointment.get();

        GetAppointmentResponse response = GetAppointmentResponse.builder()
                .clientName(appointment.getClientName())
                .clientPhoneNumber(appointment.getClientPhoneNumber())
                .clientEmail(appointment.getClientEmail())
                .description(appointment.getDescription())
                .licensePlate(appointment.getLicensePlate())
                .endTime(appointment.getEndTime())
                .startTime(appointment.getStartTime())
                .employees(appointment.getEmployees().stream().map(EmployeeConverter::convert).toList())
                .location(appointment.getLocation())
                .build();

        return response;
    }
}
