package nl.fontys.sioux.siouxbackend.business.interf.appointment;

import nl.fontys.sioux.siouxbackend.domain.response.appointment.GetAppointmentResponse;

public interface GetAppointmentUseCase {
    GetAppointmentResponse getAppointment(Long id);
}
