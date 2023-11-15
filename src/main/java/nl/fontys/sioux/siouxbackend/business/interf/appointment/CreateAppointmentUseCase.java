package nl.fontys.sioux.siouxbackend.business.interf.appointment;

import nl.fontys.sioux.siouxbackend.domain.request.appointment.CreateAppointmentRequest;
import nl.fontys.sioux.siouxbackend.domain.response.appointment.CreateAppointmentResponse;

public interface CreateAppointmentUseCase {
    CreateAppointmentResponse createAppointment(CreateAppointmentRequest request);
}
