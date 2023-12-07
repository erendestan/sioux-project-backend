package nl.fontys.sioux.siouxbackend.business.interf.appointment;

import nl.fontys.sioux.siouxbackend.domain.request.appointment.DeleteAppointmentRequest;

public interface DeleteAppointmentUseCase {
    void deleteAppointment(Long id, DeleteAppointmentRequest request);
}
