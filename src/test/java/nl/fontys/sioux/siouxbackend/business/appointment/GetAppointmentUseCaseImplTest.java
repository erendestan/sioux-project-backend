package nl.fontys.sioux.siouxbackend.business.appointment;

import nl.fontys.sioux.siouxbackend.business.exception.InvalidAppointmentException;
import nl.fontys.sioux.siouxbackend.business.impl.appointment.GetAppointmentUseCaseImpl;
import nl.fontys.sioux.siouxbackend.domain.DTO.GetAppointmentEmployeeDTO;
import nl.fontys.sioux.siouxbackend.domain.Employee;
import nl.fontys.sioux.siouxbackend.domain.Position;
import nl.fontys.sioux.siouxbackend.domain.response.appointment.GetAppointmentResponse;
import nl.fontys.sioux.siouxbackend.repository.AppointmentRepository;
import nl.fontys.sioux.siouxbackend.repository.entity.AppointmentEntity;
import nl.fontys.sioux.siouxbackend.repository.entity.EmployeeEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetAppointmentUseCaseImplTest {
    @Mock
    private AppointmentRepository appointmentRepository;
    @InjectMocks
    private GetAppointmentUseCaseImpl getAppointmentUseCase;

    @Test
    void getAppointment_shouldReturnGetAppointmentResponse_whenIdValid(){
        EmployeeEntity employee = EmployeeEntity.builder()
                .id(1L)
                .firstName("Megan")
                .lastName("Nicole")
                .active(true)
                .email("megan@gmail.com")
                .password("123")
                .position(Position.Employee)
                .build();
        AppointmentEntity appointment = AppointmentEntity.builder()
                .id(1L)
                .clientName("Kinara")
                .clientEmail("k@gmail.com")
                .clientPhoneNumber("+314892374344")
                .description("meeting")
                .employees(List.of(employee))
                .startTime(new Date(2023, Calendar.MARCH, 24, 7, 30))
                .endTime(new Date(2023, Calendar.MARCH, 24, 8, 0))
                .licensePlate("TY-ABC-1")
                .location("R10")
                .build();

        GetAppointmentEmployeeDTO employeeDTO = GetAppointmentEmployeeDTO.builder()
                .id(1L)
                .firstName("Megan")
                .lastName("Nicole")
                .build();

        GetAppointmentResponse expected = GetAppointmentResponse.builder()
                .clientName("Kinara")
                .employees(List.of(employeeDTO))
                .clientEmail("k@gmail.com")
                .clientPhoneNumber("+314892374344")
                .description("meeting")
                .startTime(new Date(2023, Calendar.MARCH, 24, 7, 30))
                .endTime(new Date(2023, Calendar.MARCH, 24, 8, 0))
                .licensePlate("TY-ABC-1")
                .location("R10")
                .build();

        when(appointmentRepository.existsById(1L)).thenReturn(true);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        GetAppointmentResponse actual = getAppointmentUseCase.getAppointment(1L);

        assertEquals(expected, actual);

        verify(appointmentRepository).existsById(1L);
        verify(appointmentRepository).findById(1L);
    }

    @Test
    void getAppointment_shouldThrowInvalidAppointmentException_whenIdInvalid(){
        when(appointmentRepository.existsById(1L)).thenReturn(false);

        assertThrows(InvalidAppointmentException.class, () -> getAppointmentUseCase.getAppointment(1L));

        verify(appointmentRepository).existsById(1L);
    }

    @Test
    void getAppointment_shouldThrowInvalidAppointmentException_whenOptionalEmpty(){
        when(appointmentRepository.existsById(1L)).thenReturn(true);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(InvalidAppointmentException.class, () -> getAppointmentUseCase.getAppointment(1L));

        verify(appointmentRepository).existsById(1L);
        verify(appointmentRepository).findById(1L);
    }
}
