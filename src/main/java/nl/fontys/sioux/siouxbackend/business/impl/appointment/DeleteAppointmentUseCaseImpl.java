package nl.fontys.sioux.siouxbackend.business.impl.appointment;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.exception.InvalidAppointmentException;
import nl.fontys.sioux.siouxbackend.business.interf.appointment.DeleteAppointmentUseCase;
import nl.fontys.sioux.siouxbackend.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteAppointmentUseCaseImpl implements DeleteAppointmentUseCase {
    private final AppointmentRepository appointmentRepository;

    @Transactional
    @Override
    public void deleteAppointment(Long id){
        if(!appointmentRepository.existsById(id)){
            throw new InvalidAppointmentException("APPOINTMENT_ID_INVALID");
        }

        appointmentRepository.deleteById(id);
    }
}
