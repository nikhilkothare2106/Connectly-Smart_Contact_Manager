package com.scm.SCMProject.controller;

import com.scm.SCMProject.entity.Contact;
import com.scm.SCMProject.entity.User;
import com.scm.SCMProject.forms.ContactForm;
import com.scm.SCMProject.forms.ContactSearchForm;
import com.scm.SCMProject.helper.AuthenticatedUserDetails;
import com.scm.SCMProject.helper.Message;
import com.scm.SCMProject.helper.MessageType;
import com.scm.SCMProject.service.ContactService;
import com.scm.SCMProject.service.ImageService;
import com.scm.SCMProject.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/user/contacts")
public class ContactController {
    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @Value("${spring.url}")
    private String url;

    private static Contact getContact(ContactForm contactForm, User user, String fileUrl, String fileName) {
        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setFavorite(contactForm.isFavorite());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setPicture(fileUrl);
        contact.setCloudinaryImagePublicId(fileName);
        return contact;
    }

    @GetMapping("/add")
    public String addContactView(Model model) {
        model.addAttribute("contactForm", new ContactForm());
        return "user/add_contact";
    }

    @PostMapping("/add")
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result,
            Authentication authentication,
            HttpSession session) {
        if (result.hasErrors()) {
            log.info(String.valueOf(result.getAllErrors()));
            session.setAttribute("message",
                    Message.builder().type(MessageType.red).content("Please fill all the fields correctly").build());
            return "user/add_contact";
        }
        String username = AuthenticatedUserDetails.getEmailOfLoggedInUser(authentication);
        System.out.println(username + " got username");

        User user = userService.getUserByEmail(username);

        // image processing

        Contact contact;
        MultipartFile file = contactForm.getContactImage();
        if (file == null || file.isEmpty()) {
            contact = getContact(contactForm, user,
                    "https://i.pinimg.com/564x/57/00/c0/5700c04197ee9a4372a35ef16eb78f4e.jpg", null);
        } else {
            System.out.println(contactForm.getContactImage().getOriginalFilename());

            String fileName = UUID.randomUUID().toString();

            // upload image
            String fileUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);

            contact = getContact(contactForm, user, fileUrl, fileName);
        }

        System.out.println(contact + " got contact");
        contactService.save(contact);

        session.setAttribute("message",
                Message.builder().type(MessageType.green).content("Contact saved successfully").build());
        return "redirect:/user/contacts/add";

    }

    // view contacts
    @RequestMapping()
    public String viewContacts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "8") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction, Model model,
            Authentication authentication) {
        String username = AuthenticatedUserDetails.getEmailOfLoggedInUser(authentication);

        User user = userService.getUserByEmail(username);
        Page<Contact> pageContact = contactService.getByUser(user, page, size, sortBy, direction);
        model.addAttribute("pageContact", pageContact);
        model.addAttribute("contactSearchForm", new ContactSearchForm());
        model.addAttribute("domainUrl", url);

        return "user/contacts";
    }

    // search handler
    @RequestMapping("/search")
    public String searchContacts(
            @ModelAttribute ContactSearchForm contactSearchForm,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "3") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction, Model model,
            Authentication authentication

    ) {

        String field = contactSearchForm.getField();
        String keyword = contactSearchForm.getKeyword();
        User user = userService.getUserByEmail(AuthenticatedUserDetails.getEmailOfLoggedInUser(authentication));
        Page<Contact> pageContact = null;
        if (field.equalsIgnoreCase("name")) {
            pageContact = contactService.searchByName(keyword, size, page, sortBy, direction, user);
        } else if (field.equalsIgnoreCase("email")) {
            pageContact = contactService.searchByEmail(keyword, size, page, sortBy, direction, user);
        } else if (field.equalsIgnoreCase("phone")) {
            pageContact = contactService.searchByPhone(keyword, size, page, sortBy, direction, user);
        }
        model.addAttribute("contactSearchForm", contactSearchForm);
        model.addAttribute("pageContact", pageContact);
        return "user/search";
    }

    @RequestMapping("/delete/{id}")
    public String deleteContact(@PathVariable String id, HttpSession session) {
        contactService.delete(id);
        session.setAttribute("message",
                Message.builder().type(MessageType.green).content("Contact deleted successfully").build());
        return "redirect:/user/contacts";
    }

    @GetMapping("/view/{id}")
    public String viewContact(@PathVariable String id, Model model) {
        Contact contact = contactService.getById(id);
        ContactForm contactForm = new ContactForm();
        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setFavorite(contact.isFavorite());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setLinkedInLink(contact.getLinkedInLink());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setPicture(contact.getPicture());
        model.addAttribute("contactForm", contactForm);
        model.addAttribute("id", id);
        return "user/update_contact_view";
    }

    @PostMapping("/update/{id}")
    public String updateContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result,
            @PathVariable String id, HttpSession session) {
        if (result.hasErrors()) {
            session.setAttribute("message",
                    Message.builder().type(MessageType.red).content("Please fill all the fields correctly").build());
            return "user/update_contact_view";
        }
        contactService.update(id, contactForm);
        session.setAttribute("message",
                Message.builder().type(MessageType.green).content("Contact updated successfully").build());
        return "redirect:/user/contacts/view/" + id;
    }

}
