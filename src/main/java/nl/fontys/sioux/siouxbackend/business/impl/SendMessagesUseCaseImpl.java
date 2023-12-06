package nl.fontys.sioux.siouxbackend.business.impl;

import lombok.AllArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.interf.SendMessagesUseCase;
import nl.fontys.sioux.siouxbackend.domain.request.parking.ParkingAccessRequest;
import nl.fontys.sioux.siouxbackend.repository.AppointmentRepository;
import nl.fontys.sioux.siouxbackend.repository.entity.AppointmentEntity;
import nl.fontys.sioux.siouxbackend.repository.entity.EmployeeEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SendMessagesUseCaseImpl implements SendMessagesUseCase {
    private final AppointmentRepository appointmentRepository;
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
        List<EmployeeEntity> employees = appointment.get().getEmployees();

        return "Appointment found but parking not full.";
    }
}
