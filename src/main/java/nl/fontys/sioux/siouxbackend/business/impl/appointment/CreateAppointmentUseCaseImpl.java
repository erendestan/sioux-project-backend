package nl.fontys.sioux.siouxbackend.business.impl.appointment;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.exception.InvalidAppointmentException;
import nl.fontys.sioux.siouxbackend.business.exception.InvalidEmployeeException;
import nl.fontys.sioux.siouxbackend.business.interf.appointment.CreateAppointmentUseCase;
import nl.fontys.sioux.siouxbackend.business.interf.appointment.SendAppointmentEmailUseCase;
import nl.fontys.sioux.siouxbackend.domain.request.appointment.CreateAppointmentRequest;
import nl.fontys.sioux.siouxbackend.domain.response.appointment.CreateAppointmentResponse;
import nl.fontys.sioux.siouxbackend.repository.AppointmentRepository;
import nl.fontys.sioux.siouxbackend.repository.EmployeeRepository;
import nl.fontys.sioux.siouxbackend.repository.entity.AppointmentEntity;
import nl.fontys.sioux.siouxbackend.repository.entity.EmployeeEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CreateAppointmentUseCaseImpl implements CreateAppointmentUseCase {
    private final AppointmentRepository appointmentRepository;
    private final EmployeeRepository employeeRepository;
    private final SendAppointmentEmailUseCase sendAppointmentEmailUseCase;

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

        List<EmployeeEntity> employees = request.getEmployeeIDs()
                .stream()
                .map(employeeRepository::getReferenceById)
                .toList();

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

        sendClientEmail(savedAppointment);
        sendEmployeesEmail(employees, savedAppointment);


        return CreateAppointmentResponse.builder()
                .appointmentID(savedAppointment.getId())
                .build();
    }

    private List<String> getEmployeeEmails(List<EmployeeEntity> employeeEntities){
        List<String> emails = new ArrayList<>();

        for(EmployeeEntity emp: employeeEntities){
            emails.add(emp.getEmail());
        }

        return emails;
    }

    private void sendClientEmail(AppointmentEntity appointment){
        String subject = "Sioux Appointment Created!";
        StringBuilder employeesList = new StringBuilder();
        for (EmployeeEntity employee : appointment.getEmployees()) {
            employeesList.append("<li>").append(employee.getFirstName()).append(" ").append(employee.getLastName()).append("</li>");
        }

        String htmlTemplate = """
                <html>
                    <body>
                        <h1>Appointment Confirmation</h1>
                        <p>Dear %s, your appointment has been scheduled.</p>
                                
                        <h3>Appointment Details</h3>
                        <p>Attendees: </p>
                        <ul>
                            %s
                        </ul>
                        <p>Start Time: %s</p>
                        <p>End Time: %s</p>
                        <p>Location: %s</p>
                        <p>Description: %s</p>
                    </body>
                </html>
                """;

        String emailBody = String.format(htmlTemplate, appointment.getClientName(), employeesList, appointment.getStartTime().toString(), appointment.getEndTime().toString(), appointment.getLocation(), appointment.getDescription());

        sendAppointmentEmailUseCase.sendAppointmentConfirmation(List.of(appointment.getClientEmail()), subject, emailBody);
    }

    private void sendEmployeesEmail(List<EmployeeEntity> employees, AppointmentEntity appointment){
        List<String> emails = getEmployeeEmails(employees);
        String subject = "Appointment With Client Created!";

        String htmlTemplate = """
                <html>
                    <body>
                        <h1>Appointment Confirmation</h1>
                        <p>Dear Employee, your appointment has been scheduled.</p>
                                
                        <h3>Appointment Details</h3>
                        <p>Client: %s</p>
                        <p>Start Time: %s</p>
                        <p>End Time: %s</p>
                        <p>Location: %s</p>
                        <p>Description: %s</p>
                    </body>
                </html>
                """;

        String emailBody = String.format(htmlTemplate, appointment.getClientName(), appointment.getStartTime().toString(), appointment.getEndTime().toString(), appointment.getLocation(), appointment.getDescription());
        sendAppointmentEmailUseCase.sendAppointmentConfirmation(emails, subject, emailBody);
    }
}
