package com.scm.SCMProject.controller;

import com.scm.SCMProject.entity.User;
import com.scm.SCMProject.forms.EditForm;
import com.scm.SCMProject.helper.AuthenticatedUserDetails;
import com.scm.SCMProject.helper.Message;
import com.scm.SCMProject.helper.MessageType;
import com.scm.SCMProject.service.ContactService;
import com.scm.SCMProject.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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


     @GetMapping("/update")
    public String editUser() {
        return "user/update";
    }

    @PostMapping("/edit")
    public String registerUser(@Valid @ModelAttribute EditForm editForm, BindingResult rBindingResult,
            HttpSession session) {
        if (rBindingResult.hasErrors()) {
            return "user/update";
        }

        User user = userService.updateUser(editForm);

        // System.out.println("User Updated! " + user);

        Message message = Message.builder().content("Profile Updated Successfully! ")
                .type(MessageType.green).build();
        session.setAttribute("message", message);

        return "redirect:/user/update";
    }
}
