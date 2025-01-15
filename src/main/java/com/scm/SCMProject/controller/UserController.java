package com.scm.SCMProject.controller;

import com.scm.SCMProject.entity.User;
import com.scm.SCMProject.helper.AuthenticatedUserDetails;
import com.scm.SCMProject.service.ContactService;
import com.scm.SCMProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @Autowired
    private ContactService contactService;
    @RequestMapping(value = "/dashboard")
    public String userDashboard1(Authentication authentication, Model model) {

        String username = AuthenticatedUserDetails.getEmailOfLoggedInUser(authentication);

        User user = userService.getUserByEmail(username);

        long totalContacts = contactService.countContacts(user.getUserId());
        model.addAttribute("totalContacts", totalContacts);
        return "user/dashboard";
    }

    @RequestMapping("/profile")
    public String userProfile() {
        return "user/profile";
    }
}
