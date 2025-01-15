package com.scm.SCMProject.service.impl;

import com.scm.SCMProject.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.properties.domain_name}")
    private String domainName;

    @Value("${spring.url}")
    private String url;

    @Override
    public void sendMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(domainName);
        mailSender.send(message);
    }

    @Override
    public String emailVerificationLink(String emailToken) {
        String link = url + "/auth/verify-email?token=" + emailToken;
        return link;
    }
}
