package nl.fontys.sioux.siouxbackend.business.impl.appointment;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.exception.FailedSendEmailException;
import nl.fontys.sioux.siouxbackend.business.interf.appointment.SendAppointmentEmailUseCase;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SendAppointmentEmailUseCaseImpl implements SendAppointmentEmailUseCase {
    private final JavaMailSender javaMailSender;

    @Override
    public void sendAppointmentConfirmation(List<String> to, String subject, String body) {
        for(String recipient : to){
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            try{
                helper.setTo(recipient);
                helper.setSubject(subject);
                helper.setText(body, true);
                javaMailSender.send(mimeMessage);
            }catch (MessagingException e){
                throw new FailedSendEmailException();
            }
        }
    }
}
