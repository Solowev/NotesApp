package com.example.authserver.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


@Component
public class EmailSenderUtil implements OtpSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendOtp(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@notesapp.ru");
        message.setTo(to);
        message.setSubject("Authentication code");
        message.setText("Here is your authentication code - " + code);
        try {
            javaMailSender.send(message);
        }catch(MailException e){
            e.printStackTrace();
        }
    }
}
