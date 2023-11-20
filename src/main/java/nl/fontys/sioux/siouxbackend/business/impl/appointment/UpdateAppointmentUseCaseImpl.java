package nl.fontys.sioux.siouxbackend.business.impl.appointment;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.exception.InvalidAppointmentException;
import nl.fontys.sioux.siouxbackend.business.exception.InvalidEmployeeException;
import nl.fontys.sioux.siouxbackend.business.interf.appointment.UpdateAppointmentUseCase;
import nl.fontys.sioux.siouxbackend.domain.request.appointment.UpdateAppointmentRequest;
import nl.fontys.sioux.siouxbackend.repository.AppointmentRepository;
import nl.fontys.sioux.siouxbackend.repository.EmployeeRepository;
import nl.fontys.sioux.siouxbackend.repository.entity.AppointmentEntity;
import nl.fontys.sioux.siouxbackend.repository.entity.EmployeeEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpdateAppointmentUseCaseImpl implements UpdateAppointmentUseCase {
    private final AppointmentRepository appointmentRepository;
    private final EmployeeRepository employeeRepository;
    @Transactional
    @Override
    public void updateAppointment(UpdateAppointmentRequest request) {
        if(!request.getEmployeeIDs().isEmpty()){
            for (Long id: request.getEmployeeIDs()) {
                if(!employeeRepository.existsById(id)) {
                    throw new InvalidEmployeeException("EMPLOYEE_ID_INVALID");
                }
            }
        }

        if(!appointmentRepository.existsById(request.getAppointmentID())){
            throw new InvalidAppointmentException("APPOINTMENT_ID_INVALID");
        }
        if(request.getClientPhoneNumber() != null && !CustomRegexValidator.checkPhoneNumberValid(request.getClientPhoneNumber())){
            throw new InvalidAppointmentException("INVALID_CLIENT_PHONE_NUMBER");
        }
        if(request.getLicensePlate() != null && !CustomRegexValidator.checkLicensePlateValid(request.getLicensePlate())){
            throw new InvalidAppointmentException("INVALID_CLIENT_LICENSE_PLATE");
        }

        AppointmentEntity appointment = appointmentRepository.findById(request.getAppointmentID()).get();

        if(request.getStartTime() != null && request.getEndTime() != null) {
            if(request.getStartTime().getTime() >= request.getEndTime().getTime()){
                throw new InvalidAppointmentException("INVALID_TIME");
            }
        } else if (request.getStartTime() != null){
            if(request.getStartTime().getTime() >= appointment.getEndTime().getTime()){
                throw new InvalidAppointmentException("INVALID_TIME");
            }
        } else if (request.getEndTime() != null) {
            if(appointment.getStartTime().getTime() >= request.getEndTime().getTime()){
                throw new InvalidAppointmentException("INVALID_TIME");
            }
        }

        if(request.getClientName() != null) { appointment.setClientName(request.getClientName()); }

        if(request.getClientEmail() != null) { appointment.setClientEmail(request.getClientEmail()); }

        if(request.getClientPhoneNumber() != null) { appointment.setClientPhoneNumber(request.getClientPhoneNumber()); }

        if(request.getDescription() != null) { appointment.setDescription(request.getDescription()); }

        if(request.getLocation() != null) { appointment.setLocation(request.getLocation()); }

        if(request.getLicensePlate() != null) { appointment.setLicensePlate(request.getLicensePlate()); }

        if(request.getStartTime() != null) { appointment.setStartTime(request.getStartTime()); }

        if(request.getEndTime() != null) { appointment.setEndTime(request.getEndTime()); }

        if(!request.getEmployeeIDs().isEmpty()) {
            List<Optional<EmployeeEntity>> employeesOptional = request.getEmployeeIDs()
                    .stream()
                    .map(employeeRepository::findById)
                    .toList();
            List<EmployeeEntity> employees = employeesOptional.stream().flatMap(Optional::stream).collect(Collectors.toList());
            appointment.setEmployees(employees);
        }

        appointmentRepository.save(appointment);
    }
}
