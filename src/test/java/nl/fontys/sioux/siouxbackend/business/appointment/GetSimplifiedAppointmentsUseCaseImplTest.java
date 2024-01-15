package nl.fontys.sioux.siouxbackend.business.appointment;

import nl.fontys.sioux.siouxbackend.business.exception.InvalidAppointmentException;
import nl.fontys.sioux.siouxbackend.business.exception.InvalidEmployeeException;
import nl.fontys.sioux.siouxbackend.business.impl.appointment.GetSimplifiedAppointmentsUseCaseImpl;
import nl.fontys.sioux.siouxbackend.domain.DTO.GetAppointmentEmployeeDTO;
import nl.fontys.sioux.siouxbackend.domain.Position;
import nl.fontys.sioux.siouxbackend.domain.request.appointment.GetSimplifiedAppointmentsRequest;
import nl.fontys.sioux.siouxbackend.domain.response.appointment.GetSimplifiedAppointmentsResponse;
import nl.fontys.sioux.siouxbackend.repository.AppointmentRepository;
import nl.fontys.sioux.siouxbackend.repository.EmployeeRepository;
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
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class GetSimplifiedAppointmentsUseCaseImplTest {
    @Mock
    AppointmentRepository appointmentRepository;
    @Mock
    EmployeeRepository employeeRepository;
    @InjectMocks
    GetSimplifiedAppointmentsUseCaseImpl getSimplifiedAppointmentsUseCase;

    @Test
    void getSimplifiedAppointments_shouldReturnListOfGetSimplifiedAppointmentsResponse(){
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

        GetSimplifiedAppointmentsRequest request = GetSimplifiedAppointmentsRequest.builder()
                .employeeIDs(List.of(1L))
                .startDateFilter(new Date(2023, Calendar.MARCH, 20, 7, 30))
                .endDateFilter(new Date(2023, Calendar.MARCH, 26, 7, 30))
                .build();

        GetAppointmentEmployeeDTO employeeDTO = GetAppointmentEmployeeDTO.builder()
                .id(1L)
                .firstName("Megan")
                .lastName("Nicole")
                .build();

        List<GetSimplifiedAppointmentsResponse> expected = List.of(GetSimplifiedAppointmentsResponse.builder()
                .id(1L)
                .clientName("Kinara")
                .startTime(new Date(2023, Calendar.MARCH, 24, 7, 30))
                .endTime(new Date(2023, Calendar.MARCH, 24, 8, 0))
                .employees(List.of(employeeDTO))
                .build());

        when(employeeRepository.existsById(1L)).thenReturn(true);
        when(appointmentRepository.findAppointmentsByEmployeeIdsAndTimeRange(List.of(1L), new Date(2023, Calendar.MARCH, 20, 7, 30), new Date(2023, Calendar.MARCH, 26, 7, 30))).thenReturn(List.of(Optional.of(appointment)));

        List<GetSimplifiedAppointmentsResponse> actual = getSimplifiedAppointmentsUseCase.getSimplifiedAppointments(request);

        assertEquals(expected, actual);

        verify(employeeRepository).existsById(1L);
        verify(appointmentRepository).findAppointmentsByEmployeeIdsAndTimeRange(List.of(1L), new Date(2023, Calendar.MARCH, 20, 7, 30), new Date(2023, Calendar.MARCH, 26, 7, 30));
    }

    @Test
    void getSimplifiedAppointments_shouldThrowInvalidEmployeeException(){
        GetSimplifiedAppointmentsRequest request = GetSimplifiedAppointmentsRequest.builder()
                .employeeIDs(List.of(1L))
                .startDateFilter(new Date(2023, Calendar.MARCH, 20, 7, 30))
                .endDateFilter(new Date(2023, Calendar.MARCH, 26, 7, 30))
                .build();

        when(employeeRepository.existsById(1L)).thenReturn(false);

        assertThrows(InvalidEmployeeException.class, () -> getSimplifiedAppointmentsUseCase.getSimplifiedAppointments(request));

        verify(employeeRepository).existsById(1L);
    }

    @Test
    void getSimplifiedAppointments_shouldThrowInvalidAppointmentException(){
        GetSimplifiedAppointmentsRequest request = GetSimplifiedAppointmentsRequest.builder()
                .employeeIDs(List.of(1L))
                .startDateFilter(new Date(2023, Calendar.MARCH, 20, 7, 30))
                .endDateFilter(new Date(2023, Calendar.MARCH, 26, 7, 30))
                .build();


        when(employeeRepository.existsById(1L)).thenReturn(true);
        when(appointmentRepository.findAppointmentsByEmployeeIdsAndTimeRange(List.of(1L), new Date(2023, Calendar.MARCH, 20, 7, 30), new Date(2023, Calendar.MARCH, 26, 7, 30))).thenReturn(List.of(Optional.empty()));

        assertThrows(InvalidAppointmentException.class, () -> getSimplifiedAppointmentsUseCase.getSimplifiedAppointments(request));

        verify(employeeRepository).existsById(1L);
        verify(appointmentRepository).findAppointmentsByEmployeeIdsAndTimeRange(List.of(1L), new Date(2023, Calendar.MARCH, 20, 7, 30), new Date(2023, Calendar.MARCH, 26, 7, 30));
    }
}
