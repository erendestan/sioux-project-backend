package nl.fontys.sioux.siouxbackend.business.appointment;

import nl.fontys.sioux.siouxbackend.business.impl.appointment.CreateAppointmentUseCaseImpl;
import nl.fontys.sioux.siouxbackend.business.impl.appointment.CustomRegexValidator;
import nl.fontys.sioux.siouxbackend.domain.Position;
import nl.fontys.sioux.siouxbackend.domain.request.appointment.CreateAppointmentRequest;
import nl.fontys.sioux.siouxbackend.domain.response.appointment.CreateAppointmentResponse;
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
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CreateAppointmentUseCaseImplTest {
    @Mock
    private AppointmentRepository appointmentRepositoryMock;

    @Mock
    private EmployeeRepository employeeRepositoryMock;

    @Mock
    private CustomRegexValidator customRegexValidatorMock;

    @InjectMocks
    private CreateAppointmentUseCaseImpl createAppointmentUseCase;

//    @Test
//    void createAppointment_WhenAllCorrect_ShouldCreate() {
//        EmployeeEntity employee1 = EmployeeEntity.builder()
//                .id(1L)
//                .firstName("Kinara")
//                .lastName("Safina")
//                .email("kinara@sioux.com")
//                .password("pass")
//                .position(Position.Admin)
//                .active(true)
//                .build();
//
//        CreateAppointmentRequest request = CreateAppointmentRequest.builder()
//                .clientName("Test client")
//                .employeeIDs(List.of(1L))
//                .startTime(new Date(2023, 11, 01, 15, 00, 00))
//                .endTime(new Date(2023, 11, 01, 16, 00, 00))
//                .licensePlate("AB-12-CD")
//                .description("Assessment")
//                .clientEmail("test@gmail.com")
//                .clientPhoneNumber("+316570009347")
//                .location("Room A")
//                .build();
//
//        when(employeeRepositoryMock.existsById(1L))
//                .thenReturn(true);
//        when(employeeRepositoryMock.findById(1L))
//                .thenReturn(Optional.of(employee1));
//
//        CreateAppointmentResponse response = createAppointmentUseCase.createAppointment(request);
//        assert
//    }
}
