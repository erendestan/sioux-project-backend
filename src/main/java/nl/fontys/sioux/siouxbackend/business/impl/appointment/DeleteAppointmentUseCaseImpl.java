package nl.fontys.sioux.siouxbackend.business.impl.appointment;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.exception.InvalidAppointmentException;
import nl.fontys.sioux.siouxbackend.business.interf.appointment.DeleteAppointmentUseCase;
import nl.fontys.sioux.siouxbackend.business.interf.appointment.SendAppointmentEmailUseCase;
import nl.fontys.sioux.siouxbackend.repository.AppointmentRepository;
import nl.fontys.sioux.siouxbackend.repository.entity.AppointmentEntity;
import nl.fontys.sioux.siouxbackend.repository.entity.EmployeeEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeleteAppointmentUseCaseImpl implements DeleteAppointmentUseCase {
    private final AppointmentRepository appointmentRepository;
    private final SendAppointmentEmailUseCase sendAppointmentEmailUseCase;

    @Transactional
    @Override
    public void deleteAppointment(Long id){
        if(!appointmentRepository.existsById(id)){
            throw new InvalidAppointmentException("APPOINTMENT_ID_INVALID");
        }

        AppointmentEntity appointment = appointmentRepository.findById(id).get();

        sendClientEmail(appointment);
        sendEmployeesEmail(appointment);

        appointmentRepository.deleteById(id);
    }

    private List<String> getEmployeeEmails(List<EmployeeEntity> employeeEntities){
        List<String> emails = new ArrayList<>();

        for(EmployeeEntity emp: employeeEntities){
            emails.add(emp.getEmail());
        }

        return emails;
    }

    private void sendClientEmail(AppointmentEntity appointment){
        String subject = "Sioux Appointment Cancelled";
        StringBuilder employeesList = new StringBuilder();
        for (EmployeeEntity employee : appointment.getEmployees()) {
            employeesList.append("<li>").append(employee.getFirstName()).append(" ").append(employee.getLastName()).append("</li>");
        }

        String htmlTemplate = """
                <html>
                    <body>
                        <h1>Appointment Cancelled</h1>
                        <p>Dear %s, your appointment has been cancelled.</p>
                                
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

    private void sendEmployeesEmail(AppointmentEntity appointment){
        List<String> emails = getEmployeeEmails(appointment.getEmployees());
        String subject = "Appointment With Client Cancelled";

        String htmlTemplate = """
                <html>
                    <body>
                        <h1>Appointment Cancelled</h1>
                        <p>Dear Employee, your appointment has been cancelled.</p>
                                
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
