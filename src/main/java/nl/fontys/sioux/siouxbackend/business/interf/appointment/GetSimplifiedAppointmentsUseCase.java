package nl.fontys.sioux.siouxbackend.business.interf.appointment;

import nl.fontys.sioux.siouxbackend.domain.request.appointment.GetSimplifiedAppointmentsRequest;
import nl.fontys.sioux.siouxbackend.domain.response.appointment.GetSimplifiedAppointmentsResponse;

import java.util.List;

public interface GetSimplifiedAppointmentsUseCase {
    List<GetSimplifiedAppointmentsResponse> getSimplifiedAppointments(GetSimplifiedAppointmentsRequest request);
}
