package ca.uqtr.fitbit.event.reminder;

import ca.uqtr.fitbit.dto.DeviceDto;
import ca.uqtr.fitbit.service.device.DeviceService;
import com.sendgrid.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;


@Component
public class SynchronizationEmailListener implements
        ApplicationListener<OnSynchronizationEmailEvent> {

    @Value("${spring.profiles.active}")
    private String mailService;
    private JavaMailSender mailSender;
    private DeviceService deviceService;

    @Autowired
    public SynchronizationEmailListener(JavaMailSender mailSender, DeviceService deviceService) {
        this.mailSender = mailSender;
        this.deviceService = deviceService;
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(OnSynchronizationEmailEvent event) {
        if (mailService.equals("dev"))
            this.confirmQuestionnaireSendGmail(event);
        else
            this.confirmQuestionnaireSendGrid(event);
    }

    private void confirmQuestionnaireSendGrid(OnSynchronizationEmailEvent event) throws IOException {
        DeviceDto device = event.getDevice();
        String recipientAddress = device.getPatientDevices().get(0).getPatientEmail();
        String subject = "Fitbit data synchronization";
        Email from = new Email("app158992707@heroku.com");
        Email to = new Email(recipientAddress);
        String message = "Don't forget to synchronize your data.";
        Content content = new Content("text/plain", message);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        try {
            request.method = Method.POST;
            request.endpoint = "mail/send";
            request.body = mail.build();
            sg.api(request);
        } catch (IOException ex) {
            throw ex;
        }
    }


    private void confirmQuestionnaireSendGmail(OnSynchronizationEmailEvent event) {
        DeviceDto device = event.getDevice();
        String recipientAddress = device.getPatientDevices().get(0).getPatientEmail();
        String subject = "Fitbit data synchronization";
        String message = "Don't forget to synchronize your data.";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }

}


