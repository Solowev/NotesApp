package com.example.authserver.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


@Component
public class EmailSenderUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMessage(String to, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@notesapp.ru");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try{
            javaMailSender.send(message);
        }catch(MailException ex){
            ex.printStackTrace();
        }
    }
}
