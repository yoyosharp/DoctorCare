package com.fx23121.DoctorCare.Service;

import com.fx23121.DoctorCare.Model.EmailDetails;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.FileSystem;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") private String sender;

    //send email with attachment
    @Override
    public void sendEmail(EmailDetails emailDetails) {

        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(sender);
            helper.setTo(emailDetails.getRecipient());
            helper.setText(emailDetails.getMsgBody());
            helper.setSubject(emailDetails.getSubject());

            FileSystemResource attachedFile = new FileSystemResource(new File(emailDetails.getAttachment()));

            helper.addAttachment(attachedFile.getFilename(), attachedFile);

            javaMailSender.send(mimeMessage);

        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while sending email");
        }
    }

    //send email without attachment
    @Override
    public void sendEmailWithoutAttachment(EmailDetails emailDetails) {
        try {

            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(emailDetails.getRecipient());
            mailMessage.setText(emailDetails.getMsgBody());
            mailMessage.setSubject(emailDetails.getSubject());

            javaMailSender.send(mailMessage);

        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while sending email");
        }
    }
}
