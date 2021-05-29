package com.example.authserver.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class AppConfig {
    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl jmsi = new JavaMailSenderImpl();
        jmsi.setHost("smtp.gmail.com");
        jmsi.setPort(587);
        jmsi.setUsername("auth.note.app@gmail.com");
        jmsi.setPassword("MyNotesApp12345");

        Properties props = jmsi.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return jmsi;
    }
}
