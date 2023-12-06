package nl.fontys.sioux.siouxbackend.business.appointment;

import nl.fontys.sioux.siouxbackend.business.exception.InvalidAppointmentException;
import nl.fontys.sioux.siouxbackend.business.exception.InvalidEmployeeException;
import nl.fontys.sioux.siouxbackend.business.impl.appointment.UpdateAppointmentUseCaseImpl;
import nl.fontys.sioux.siouxbackend.domain.Position;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateAppointmentUseCaseImplTest {
    @Mock
    private AppointmentRepository appointmentRepositoryMock;

    @Mock
    private EmployeeRepository employeeRepositoryMock;

    @InjectMocks
    private UpdateAppointmentUseCaseImpl updateAppointmentUseCase;

    @Test
    void updateAppointment_WhenAllCorrect_ShouldUpdate() {
        EmployeeEntity employee1 = EmployeeEntity.builder()
                .id(1L)
                .firstName("Kinara")
                .lastName("Safina")
                .email("kinara@sioux.com")
                .password("pass")
                .position(Position.Admin)
                .active(true)
                .build();
        EmployeeEntity employee2 = EmployeeEntity.builder()
                .id(2L)
                .firstName("Vlad")
                .lastName("Bucur")
                .email("vlad@sioux.com")
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

        UpdateAppointmentRequest request = UpdateAppointmentRequest.builder()
                .appointmentID(1L)
                .employeeIDs(List.of(1L, 2L))
                .description("Project assessment")
                .endTime(new Date(2023, 11, 01, 16, 30, 00))
                .clientPhoneNumber("+316578319347")
                .licensePlate("RX-57-IK")
                .build();

        when(employeeRepositoryMock.findById(1L))
                .thenReturn(Optional.of(employee1));
        when(employeeRepositoryMock.existsById(1L))
                .thenReturn(true);
        when(employeeRepositoryMock.findById(2L))
                .thenReturn(Optional.of(employee2));
        when(employeeRepositoryMock.existsById(2L))
                .thenReturn(true);
        when(appointmentRepositoryMock.findById(1L))
                .thenReturn(Optional.of(appointment));
        when(appointmentRepositoryMock.existsById(1L))
                .thenReturn(true);

        updateAppointmentUseCase.updateAppointment(request);
        verify(appointmentRepositoryMock).save(any());
    }

    @Test
    void updateAppointment_WhenEmployeeDoesNotExist_ShouldThrowInvalidEmployeeException() {
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

        UpdateAppointmentRequest request = UpdateAppointmentRequest.builder()
                .appointmentID(1L)
                .employeeIDs(List.of(1L, 2L))
                .description("Project assessment")
                .endTime(new Date(2023, 11, 01, 16, 30, 00))
                .clientPhoneNumber("+316578319347")
                .licensePlate("RX-57-IK")
                .build();

        when(employeeRepositoryMock.existsById(1L))
                .thenReturn(true);
        when(employeeRepositoryMock.existsById(2L))
                .thenReturn(false);


        assertThrows(InvalidEmployeeException.class, () -> updateAppointmentUseCase.updateAppointment(request));
        verify(appointmentRepositoryMock, never()).save(any());
    }

    @Test
    void updateAppointment_WhenAppointmentDoesNotExist_ShouldThrowInvalidAppointmentException_WithMessageAPPOINTMENT_ID_INVALID() {
        UpdateAppointmentRequest request = UpdateAppointmentRequest.builder()
                .appointmentID(1L)
                .employeeIDs(List.of(1L))
                .description("Project assessment")
                .endTime(new Date(2023, 11, 01, 16, 30, 00))
                .clientPhoneNumber("+316578319347")
                .licensePlate("RX-57-IK")
                .build();

        when(employeeRepositoryMock.existsById(1L))
                .thenReturn(true);
        when(appointmentRepositoryMock.existsById(1L))
                .thenReturn(false);

        assertThrows(InvalidAppointmentException.class, () -> updateAppointmentUseCase.updateAppointment(request), "APPOINTMENT_ID_INVALID");
        verify(appointmentRepositoryMock, never()).save(any());
    }

    @Test
    void updateAppointment_WhenPhoneNumberInvalid_ShouldThrowInvalidAppointmentException_WithMessageINVALID_CLIENT_PHONE_NUMBER() {
        UpdateAppointmentRequest request = UpdateAppointmentRequest.builder()
                .appointmentID(1L)
                .employeeIDs(List.of(1L))
                .description("Project assessment")
                .endTime(new Date(2023, 11, 01, 16, 30, 00))
                .clientPhoneNumber("not a phone number")
                .licensePlate("RX-57-IK")
                .build();

        when(employeeRepositoryMock.existsById(1L))
                .thenReturn(true);
        when(appointmentRepositoryMock.existsById(1L))
                .thenReturn(false);

        assertThrows(InvalidAppointmentException.class, () -> updateAppointmentUseCase.updateAppointment(request), "INVALID_CLIENT_PHONE_NUMBER");
        verify(appointmentRepositoryMock, never()).save(any());
    }

    @Test
    void updateAppointment_WhenLicensePlateInvalid_ShouldThrowInvalidAppointmentException_WithMessageINVALID_CLIENT_LICENSE_PLATE() {
        EmployeeEntity employee1 = EmployeeEntity.builder()
                .id(1L)
                .firstName("Kinara")
                .lastName("Safina")
                .email("kinara@sioux.com")
                .password("pass")
                .position(Position.Admin)
                .active(true)
                .build();

        UpdateAppointmentRequest request = UpdateAppointmentRequest.builder()
                .appointmentID(1L)
                .employeeIDs(List.of(1L))
                .description("Project assessment")
                .endTime(new Date(2023, 11, 01, 16, 30, 00))
                .clientPhoneNumber("+316578319347")
                .licensePlate("not a license plate")
                .build();

        when(employeeRepositoryMock.existsById(1L))
                .thenReturn(true);
        when(appointmentRepositoryMock.existsById(1L))
                .thenReturn(false);

        assertThrows(InvalidAppointmentException.class, () -> updateAppointmentUseCase.updateAppointment(request), "INVALID_CLIENT_LICENSE_PLATE");
        verify(appointmentRepositoryMock, never()).save(any());
    }

    @Test
    void updateAppointment_WhenStartAndEndTimesIncorrect_ShouldThrowInvalidAppointmentException_WithMessageINVALID_TIME() {
        EmployeeEntity employee1 = EmployeeEntity.builder()
                .id(1L)
                .firstName("Kinara")
                .lastName("Safina")
                .email("kinara@sioux.com")
                .password("pass")
                .position(Position.Admin)
                .active(true)
                .build();
        EmployeeEntity employee2 = EmployeeEntity.builder()
                .id(2L)
                .firstName("Vlad")
                .lastName("Bucur")
                .email("vlad@sioux.com")
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

        UpdateAppointmentRequest request = UpdateAppointmentRequest.builder()
                .appointmentID(1L)
                .employeeIDs(List.of(1L, 2L))
                .description("Project assessment")
                .startTime(new Date(2023, 11, 01, 17, 30, 00))
                .endTime(new Date(2023, 11, 01, 16, 30, 00))
                .clientPhoneNumber("+316578319347")
                .licensePlate("RX-57-IK")
                .build();

        when(employeeRepositoryMock.existsById(1L))
                .thenReturn(true);
        when(employeeRepositoryMock.existsById(2L))
                .thenReturn(true);
        when(appointmentRepositoryMock.findById(1L))
                .thenReturn(Optional.of(appointment));
        when(appointmentRepositoryMock.existsById(1L))
                .thenReturn(true);

        assertThrows(InvalidAppointmentException.class, () -> updateAppointmentUseCase.updateAppointment(request), "INVALID_TIME");
        verify(appointmentRepositoryMock, never()).save(any());
    }
}
