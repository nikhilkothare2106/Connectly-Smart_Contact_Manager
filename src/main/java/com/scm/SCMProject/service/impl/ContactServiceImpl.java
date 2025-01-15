package com.scm.SCMProject.service.impl;

import com.scm.SCMProject.Repository.ContactRepo;
import com.scm.SCMProject.entity.Contact;
import com.scm.SCMProject.entity.User;
import com.scm.SCMProject.forms.ContactForm;
import com.scm.SCMProject.service.ContactService;
import com.scm.SCMProject.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepo contactRepo;

    @Autowired
    private ImageService imageService;

    @Override
    public Contact save(Contact contact) {

        String contactId = UUID.randomUUID().toString();
        contact.setId(contactId);
        return contactRepo.save(contact);

    }

    @Override
    public Contact update(String id, ContactForm contactForm) {
        Contact contact = contactRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with given id " + id));
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setFavorite(contactForm.isFavorite());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());

        MultipartFile file = contactForm.getContactImage();
        if (file != null && !file.isEmpty()) {
            System.out.println(contactForm.getContactImage().getOriginalFilename());

            String fileName = UUID.randomUUID().toString();

            // upload image
            String fileUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);

            contact.setPicture(fileUrl);
            contact.setCloudinaryImagePublicId(fileName);
        }

        return contactRepo.save(contact);
    }


    @Override
    public List<Contact> getAll() {
        return contactRepo.findAll();
    }

    @Override
    public Contact getById(String id) {
        return contactRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with given id " + id));
    }

    @Transactional
    @Override
    public void delete(String id) {
        Contact contact = contactRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with given id " + id));
        System.out.println(contact.getName());
        User user = contact.getUser();
        if (user != null) {
            user.getContacts().remove(contact); // Remove contact from the user's list
        }
        contactRepo.delete(contact);


    }

    @Override
    public Page<Contact> searchByName(String name, int size, int page, String sortBy, String order, User user) {
        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUserAndNameContaining(user, name, pageable);
    }

    @Override
    public Page<Contact> searchByEmail(String email, int size, int page, String sortBy, String order, User user) {
        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUserAndEmailContaining(user, email, pageable);
    }

    @Override
    public Page<Contact> searchByPhone(String phone, int size, int page, String sortBy, String order, User user) {
        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUserAndPhoneNumberContaining(user, phone, pageable);
    }


    @Override
    public List<Contact> getByUserId(String userId) {
        return contactRepo.findByUserId(userId);

    }

    @Override
    public Page<Contact> getByUser(User user, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return contactRepo.findByUser(user, pageable);
    }

    @Override
    public long countContacts(String userId) {
        return contactRepo.countContacts(userId);
    }


}
