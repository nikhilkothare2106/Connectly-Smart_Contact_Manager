package com.scm.SCMProject.controller;

import com.scm.SCMProject.entity.Providers;
import com.scm.SCMProject.entity.User;
import com.scm.SCMProject.forms.UserForm;
import com.scm.SCMProject.helper.Message;
import com.scm.SCMProject.helper.MessageType;
import com.scm.SCMProject.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    private static User getUser(UserForm userForm) {
        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setEnabled(false);
        user.setPhoneVerified(false);
        user.setEmailVerified(false);
        user.setProvider(Providers.SELF);
        user.setProfilePic(
                "https://cdn-icons-png.flaticon.com/512/3135/3135715.png");
        return user;
    }

    @GetMapping("/")
    public String landingPage() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/about")
    public String aboutPage(Model model) {
        System.out.println("About page loading");
        return "about";
    }

    @GetMapping("/services")
    public String servicesPage() {
        System.out.println("services page loading");
        return "services";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "register";
    }

    @PostMapping("/do-register")
    public String registerUser(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult,
            HttpSession session) {
        if (rBindingResult.hasErrors()) {
            return "register";
        }

        User user1 = userService.getUserByEmail(userForm.getEmail());
        if (user1 != null) {
            Message message = Message.builder().content("User with this email already present!").type(MessageType.red)
                    .build();
            session.setAttribute("message", message);
        } else {
            User user = getUser(userForm);

            User savedUser = userService.saveUser(user);
            System.out.println("User Saved! " + savedUser);

            Message message = Message.builder().content("Registration Successful!").type(MessageType.green).build();
            session.setAttribute("message", message);
        }

        return "redirect:/register";
    }

    @GetMapping("/auth/verify-email")
    public String verifyEmail(@RequestParam("token") String token, HttpSession session) {
        User user = userService.getUserByVerificationToken(token);

        System.out.println(user + " : user");

        if (user != null) {
            user.setEmailVerified(true);
            user.setEnabled(true);
            System.out.println(user.isEnabled());
            userService.saveVerifiedUser(user);
            session.setAttribute("message",
                    Message.builder().content("Email Verified!").type(MessageType.green).build());
            return "success_page";
        }
        session.setAttribute("message", Message.builder().content("Invalid Token!").type(MessageType.red).build());
        System.out.println(user + " : user");
        return "error_page";
    }

    @PostMapping("/send-message")
    public String sendMessage(@RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("message") String message,
            Model model, HttpSession session) {
        // emailService.sendMail("nikhilkothare14@gmail.com", "Message from user", email
        // + " " +message);
        session.setAttribute("message", Message.builder().content("Message sent").type(MessageType.green).build());
        return "success_page";

    }
}
