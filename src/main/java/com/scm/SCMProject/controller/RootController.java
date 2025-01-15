package com.scm.SCMProject.controller;

import com.scm.SCMProject.entity.User;
import com.scm.SCMProject.helper.AuthenticatedUserDetails;
import com.scm.SCMProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class RootController {
    @Autowired
    UserService userService;

    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication) {
        String username = AuthenticatedUserDetails.getEmailOfLoggedInUser(authentication);
        if (username.isEmpty()) {
            return;
        }
        User user = userService.getUserByEmail(username);

        String firstName = user.getName().split(" ")[0];
        model.addAttribute("firstName", firstName);
        model.addAttribute("User", user);
    }
}
