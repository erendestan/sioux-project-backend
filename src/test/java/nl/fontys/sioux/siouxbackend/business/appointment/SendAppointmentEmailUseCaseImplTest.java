package nl.fontys.sioux.siouxbackend.business.appointment;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.Table;
import nl.fontys.sioux.siouxbackend.business.impl.appointment.SendAppointmentEmailUseCaseImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SendAppointmentEmailUseCaseImplTest {
    @Mock
    JavaMailSender javaMailSender;

    @InjectMocks
    SendAppointmentEmailUseCaseImpl sendAppointmentEmailUseCase;

    @Test
    void sendAppointmentConfirmation_shouldSendEmail(){
        MimeMessage mimeMessage = new MimeMessage((Session) null);

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        sendAppointmentEmailUseCase.sendAppointmentConfirmation(List.of("kinara@gmail.com"), "Appointment Created", "body");

        verify(javaMailSender).createMimeMessage();
        verify(javaMailSender).send(mimeMessage);
    }
}
