package nl.fontys.sioux.siouxbackend.business.impl.appointment;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.interf.appointment.CreateAppointmentUseCase;
import nl.fontys.sioux.siouxbackend.domain.request.appointment.CreateAppointmentRequest;
import nl.fontys.sioux.siouxbackend.domain.response.appointment.CreateAppointmentResponse;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateAppointmentUseCaseImpl implements CreateAppointmentUseCase {

    @Transactional
    @Override
    public CreateAppointmentResponse createAppointment(CreateAppointmentRequest request) {
        throw new NotImplementedException();
    }

}
