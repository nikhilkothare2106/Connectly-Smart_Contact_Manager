package com.scm.SCMProject.service;

public interface EmailService {

    void sendMail(String to, String subject, String body);

    String emailVerificationLink(String token);
}
