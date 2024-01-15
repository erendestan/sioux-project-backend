package nl.fontys.sioux.siouxbackend.business.impl;

import lombok.AllArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.interf.SendMessagesUseCase;
import nl.fontys.sioux.siouxbackend.business.interf.appointment.SendAppointmentEmailUseCase;
import nl.fontys.sioux.siouxbackend.domain.request.parking.ParkingAccessRequest;
import nl.fontys.sioux.siouxbackend.repository.AppointmentRepository;
import nl.fontys.sioux.siouxbackend.repository.entity.AppointmentEntity;
import nl.fontys.sioux.siouxbackend.repository.entity.EmployeeEntity;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SendMessagesUseCaseImpl implements SendMessagesUseCase {
    private final AppointmentRepository appointmentRepository;
    private final SendAppointmentEmailUseCase sendAppointmentEmailUseCase;
    private static final String ACCOUNT_SID = "AC13ebc0b0a78e5feb2d84484ccf289988";
    private static final String AUTH_TOKEN = "15461a02d41ca998e192de2ddff4d53b";
    private static final String TWILIO_PHONE_NUMBER = "+12512164793";
    @Override
    public String handleUpcomingAppointmentNotifications(ParkingAccessRequest request) {
        // Get current time
        Date now = new Date();

        // Get current time plus 1 hour
        long oneHourInMillis = 60 * 60 * 1000; // 60 seconds/minute * 60 minutes/hour * 1000 milliseconds/second
        Date oneHourLater = new Date(now.getTime() + oneHourInMillis);

//        // Find appointment starting in the next hour for a specific license plate
//        Optional<AppointmentEntity> appointment = appointmentRepository.findFirstByStartTimeBetweenAndLicensePlate
//                (now, oneHourLater, request.getLicensePlate());

        // Find appointment starting in the next hour for a specific license plate
        Optional<AppointmentEntity> appointments = appointmentRepository.findAllByStartTimeBetween(now, oneHourLater);

        Optional<AppointmentEntity> matchingAppointment = appointments.stream()
                .filter(appointment -> isLicensePlateSimilar(request.getLicensePlate(), appointment.getLicensePlate()))
                .findFirst();


        if (matchingAppointment.isEmpty()) {
            return "No appointment found!";
        }

        if (request.isParkingTaken()) {
            // TODO: Send message to client to go to other parking lot
            String clientPhoneNumber = matchingAppointment.get().getClientPhoneNumber();

            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);


            String messageBody = "Welcome to Sioux! Our parking lot is currently full. Here are directions to the main parking lot: \n https://maps.app.goo.gl/EdCSYZggnNtexCEs5";

            Message message = Message.creator(
                            new PhoneNumber(formatPhoneNumber(clientPhoneNumber)),
                            new PhoneNumber(TWILIO_PHONE_NUMBER),
                            messageBody)
                    .create();
            System.out.println("Message SID: " + message.getSid());

            sendEmployeesEmail(matchingAppointment.get(), request);
            return "Sent message to client at: " + clientPhoneNumber;
        }

        // TODO OPTIONAL: Send message (or email) to employees in appointment that client has arrived
        //                (and maybe at which parking lot)

        sendEmployeesEmail(matchingAppointment.get(), request);

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

    public String formatPhoneNumber(String phoneNumber) {
        // Check if the phone number is not empty
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            // Check if the phone number starts with "0"
            if (phoneNumber.startsWith("0")) {
                // Remove the leading "0" and prepend "+31"
                phoneNumber = "+31" + phoneNumber.substring(1);
            } else {
                // If it doesn't start with "0", assume it's already in the correct format
                phoneNumber = "+" + phoneNumber;
            }
        }

        return phoneNumber;
    }

    private boolean isLicensePlateSimilar(String plate1, String plate2) {
        int distanceThreshold = 1; // You can adjust this threshold as needed
        int distance =  LevenshteinDistance.getDefaultInstance().apply(plate1, plate2);
        return distance <= distanceThreshold;
    }
}
