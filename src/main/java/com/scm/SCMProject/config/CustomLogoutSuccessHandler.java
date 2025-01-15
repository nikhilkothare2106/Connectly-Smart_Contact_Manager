package com.scm.SCMProject.config;

import com.scm.SCMProject.helper.Message;
import com.scm.SCMProject.helper.MessageType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication != null) {
            // Perform your custom logic here
            System.out.println("User " + authentication.getName() + " has logged out.");
            HttpSession session = request.getSession();
            session.setAttribute("message", Message.builder().content("You have been logged out!").type(MessageType.red).build());
        }

        response.sendRedirect("/login?logout");
    }
}
