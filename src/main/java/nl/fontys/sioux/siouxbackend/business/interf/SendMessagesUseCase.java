package nl.fontys.sioux.siouxbackend.business.interf;

import nl.fontys.sioux.siouxbackend.domain.request.parking.ParkingAccessRequest;

public interface SendMessagesUseCase {
    String handleUpcomingAppointmentNotifications(ParkingAccessRequest request);
}
