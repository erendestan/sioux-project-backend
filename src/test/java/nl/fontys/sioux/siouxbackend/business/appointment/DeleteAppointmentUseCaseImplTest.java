package nl.fontys.sioux.siouxbackend.business.appointment;

import nl.fontys.sioux.siouxbackend.business.exception.InvalidAppointmentException;
import nl.fontys.sioux.siouxbackend.business.exception.InvalidEmployeeException;
import nl.fontys.sioux.siouxbackend.business.impl.appointment.DeleteAppointmentUseCaseImpl;
import nl.fontys.sioux.siouxbackend.domain.Position;
import nl.fontys.sioux.siouxbackend.domain.request.appointment.DeleteAppointmentRequest;
import nl.fontys.sioux.siouxbackend.domain.request.appointment.UpdateAppointmentRequest;
import nl.fontys.sioux.siouxbackend.repository.AppointmentRepository;
import nl.fontys.sioux.siouxbackend.repository.EmployeeRepository;
import nl.fontys.sioux.siouxbackend.repository.entity.AppointmentEntity;
import nl.fontys.sioux.siouxbackend.repository.entity.EmployeeEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteAppointmentUseCaseImplTest {
    @Mock
    private AppointmentRepository appointmentRepositoryMock;

    @InjectMocks
    private DeleteAppointmentUseCaseImpl deleteAppointmentUseCase;

    @Test
    void deleteAppointment_WhenAppointmentExists_ShouldDelete() {
        EmployeeEntity employee1 = EmployeeEntity.builder()
                .id(1L)
                .firstName("Kinara")
                .lastName("Safina")
                .email("kinara@sioux.com")
                .password("pass")
                .position(Position.Admin)
                .active(true)
                .build();

        AppointmentEntity appointment = AppointmentEntity.builder()
                .id(1L)
                .clientName("Test client")
                .employees(List.of(employee1))
                .startTime(new Date(2023, 11, 01, 15, 00, 00))
                .endTime(new Date(2023, 11, 01, 16, 00, 00))
                .licensePlate("AB-12-CD")
                .description("Assessment")
                .clientEmail("test@gmail.com")
                .clientPhoneNumber("+316570009347")
                .location("Room A")
                .build();

        when(appointmentRepositoryMock.existsById(1L))
                .thenReturn(true);

        DeleteAppointmentRequest request = DeleteAppointmentRequest.builder().reason("sick").build();

        deleteAppointmentUseCase.deleteAppointment(1L, request);
        verify(appointmentRepositoryMock).deleteById(any());
    }

    @Test
    void deleteAppointment_WhenAppointmentDoesNotExist_ShouldThrowInvalidAppointmentException() {
        when(appointmentRepositoryMock.existsById(1L))
                .thenReturn(false);

        DeleteAppointmentRequest request = DeleteAppointmentRequest.builder().reason("sick").build();

        assertThrows(InvalidAppointmentException.class, () -> deleteAppointmentUseCase.deleteAppointment(1L, request));
        verify(appointmentRepositoryMock, never()).deleteById(any());
    }

}
