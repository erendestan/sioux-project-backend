package nl.fontys.sioux.siouxbackend.business.interf.appointment;

import java.util.List;

public interface SendAppointmentEmailUseCase {
    void sendAppointmentConfirmation(List<String> to, String subject, String body);
}
