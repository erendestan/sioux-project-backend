package nl.fontys.sioux.siouxbackend.business.impl;

import lombok.AllArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.interf.SendMessagesUseCase;
import nl.fontys.sioux.siouxbackend.business.interf.appointment.SendAppointmentEmailUseCase;
import nl.fontys.sioux.siouxbackend.domain.request.parking.ParkingAccessRequest;
import nl.fontys.sioux.siouxbackend.repository.AppointmentRepository;
import nl.fontys.sioux.siouxbackend.repository.entity.AppointmentEntity;
import nl.fontys.sioux.siouxbackend.repository.entity.EmployeeEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SendMessagesUseCaseImpl implements SendMessagesUseCase {
    private final AppointmentRepository appointmentRepository;
    private final SendAppointmentEmailUseCase sendAppointmentEmailUseCase;
    @Override
    public String handleUpcomingAppointmentNotifications(ParkingAccessRequest request) {
        // Get current time
        Date now = new Date();

        // Get current time plus 1 hour
        long oneHourInMillis = 60 * 60 * 1000; // 60 seconds/minute * 60 minutes/hour * 1000 milliseconds/second
        Date oneHourLater = new Date(now.getTime() + oneHourInMillis);

        // Find appointment starting in the next hour for a specific license plate
        Optional<AppointmentEntity> appointment = appointmentRepository.findFirstByStartTimeBetweenAndLicensePlate
                (now, oneHourLater, request.getLicensePlate());

        if (appointment.isEmpty()) {
            return "No appointment found!";
        }

        if (request.isParkingTaken()) {
            // TODO: Send message to client to go to other parking lot
            String clientPhoneNumber = appointment.get().getClientPhoneNumber();
            return "Sent message to client at: " + clientPhoneNumber;
        }

        // TODO OPTIONAL: Send message (or email) to employees in appointment that client has arrived
        //                (and maybe at which parking lot)

        sendEmployeesEmail(appointment.get(), request);

        return "Appointment found but parking not full.";
    }

    private List<String> getEmployeeEmails(List<EmployeeEntity> employeeEntities){
        List<String> emails = new ArrayList<>();

        for(EmployeeEntity emp: employeeEntities){
            emails.add(emp.getEmail());
        }

        return emails;
    }

    private void sendEmployeesEmail(AppointmentEntity appointment, ParkingAccessRequest request){
        List<String> emails = getEmployeeEmails(appointment.getEmployees());
        String subject = "Your client has arrived";

        String htmlTemplate = """
                <html>
                    <body>
                        <h1>Your client has arrived</h1>
                        <p>Dear Employee, your client, %s, has arrived in parking lot %s</p>
                                
                        <h3>Appointment Details</h3>
                        <p>Client: %s</p>
                        <p>Start Time: %s</p>
                        <p>End Time: %s</p>
                        <p>Location: %s</p>
                        <p>Description: %s</p>
                    </body>
                </html>
                """;

        String emailBody = String.format(htmlTemplate, appointment.getClientName(), request.isParkingTaken()? "B" : "A",appointment.getClientName(), appointment.getStartTime().toString(), appointment.getEndTime().toString(), appointment.getLocation(), appointment.getDescription());
        sendAppointmentEmailUseCase.sendAppointmentConfirmation(emails, subject, emailBody);
    }
}
