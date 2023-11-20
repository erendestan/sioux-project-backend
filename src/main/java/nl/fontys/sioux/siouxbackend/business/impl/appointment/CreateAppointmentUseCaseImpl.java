package nl.fontys.sioux.siouxbackend.business.impl.appointment;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.exception.InvalidAppointmentException;
import nl.fontys.sioux.siouxbackend.business.exception.InvalidEmployeeException;
import nl.fontys.sioux.siouxbackend.business.interf.appointment.CreateAppointmentUseCase;
import nl.fontys.sioux.siouxbackend.domain.request.appointment.CreateAppointmentRequest;
import nl.fontys.sioux.siouxbackend.domain.response.appointment.CreateAppointmentResponse;
import nl.fontys.sioux.siouxbackend.repository.AppointmentRepository;
import nl.fontys.sioux.siouxbackend.repository.EmployeeRepository;
import nl.fontys.sioux.siouxbackend.repository.entity.AppointmentEntity;
import nl.fontys.sioux.siouxbackend.repository.entity.EmployeeEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CreateAppointmentUseCaseImpl implements CreateAppointmentUseCase {
    private final AppointmentRepository appointmentRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    @Override
    public CreateAppointmentResponse createAppointment(CreateAppointmentRequest request) {
        for (Long id: request.getEmployeeIDs()) {
            if(!employeeRepository.existsById(id)) {
                throw new InvalidEmployeeException("EMPLOYEE_ID_INVALID");
            }
        }
        if(!CustomRegexValidator.checkPhoneNumberValid(request.getClientPhoneNumber())){
            throw new InvalidAppointmentException("INVALID_CLIENT_PHONE_NUMBER");
        }
        if(!CustomRegexValidator.checkLicensePlateValid(request.getLicensePlate())){
            throw new InvalidAppointmentException("INVALID_CLIENT_LICENSE_PLATE");
        }
        if(request.getStartTime().getTime() >= request.getEndTime().getTime()){
            throw new InvalidAppointmentException("INVALID_TIME");
        }
        if(appointmentRepository.existsAppointmentForEmployeesInTimeRange(request.getEmployeeIDs(), request.getStartTime(), request.getEndTime())) {
            throw new InvalidAppointmentException("EMPLOYEE_TIMESLOT_ALREADY_TAKEN");
        }

        List<EmployeeEntity> employees = request.getEmployeeIDs()
                .stream()
                .map(employeeRepository::findById)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        AppointmentEntity appointment = AppointmentEntity.builder()
                .clientName(request.getClientName())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .employees(employees)
                .clientPhoneNumber(request.getClientPhoneNumber())
                .location(request.getLocation())
                .description(request.getDescription())
                .licensePlate(request.getLicensePlate())
                .clientEmail(request.getClientEmail())
                .build();

        AppointmentEntity savedAppointment = appointmentRepository.save(appointment);
        return CreateAppointmentResponse.builder()
                .appointmentID(savedAppointment.getId())
                .build();
    }
}
