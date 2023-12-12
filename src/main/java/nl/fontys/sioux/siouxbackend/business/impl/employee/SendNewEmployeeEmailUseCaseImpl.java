package nl.fontys.sioux.siouxbackend.business.impl.employee;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import nl.fontys.sioux.siouxbackend.business.exception.FailedSendEmailException;
import nl.fontys.sioux.siouxbackend.business.interf.employee.SendNewEmployeeEmailUseCase;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SendNewEmployeeEmailUseCaseImpl implements SendNewEmployeeEmailUseCase {
    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmployeeCreatedConfirmation(String to, String subject, String body) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        try{
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            javaMailSender.send(mimeMessage);
        }catch (MessagingException e){
            throw new FailedSendEmailException();
        }
    }
}
