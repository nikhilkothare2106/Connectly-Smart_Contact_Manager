package com.scm.SCMProject.config;

import com.scm.SCMProject.helper.Message;
import com.scm.SCMProject.helper.MessageType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationFailureHandler implements org.springframework.security.web.authentication.AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        System.out.println("Authentication failed " + exception);
        HttpSession session = request.getSession();
        if (exception instanceof DisabledException) {

            session.setAttribute("message", Message
                    .builder().content("User is disabled, Verification link is sent on your Email !").type(MessageType.red).build());


        } else {
            session.setAttribute("message", Message
                    .builder().content("Invalid Credentials").type(MessageType.red).build());


        }
        response.sendRedirect("/login?error=true");
    }
}
