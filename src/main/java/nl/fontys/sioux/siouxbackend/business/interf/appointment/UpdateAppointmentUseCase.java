package nl.fontys.sioux.siouxbackend.business.interf.appointment;

import nl.fontys.sioux.siouxbackend.domain.request.appointment.UpdateAppointmentRequest;

public interface UpdateAppointmentUseCase {
    void updateAppointment(UpdateAppointmentRequest request);
}
