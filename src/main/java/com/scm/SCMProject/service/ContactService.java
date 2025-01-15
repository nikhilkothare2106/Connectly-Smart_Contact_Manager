package com.scm.SCMProject.service;

import com.scm.SCMProject.entity.Contact;
import com.scm.SCMProject.entity.User;
import com.scm.SCMProject.forms.ContactForm;
import org.springframework.data.domain.Page;

import java.util.List;


public interface ContactService {
    // save contacts
    Contact save(Contact contact);

    // update contact
    Contact update(String id, ContactForm contactForm);

    // get contacts
    List<Contact> getAll();

    // get contact by id

    Contact getById(String id);

    // delete contact

    void delete(String id);

    // search contact
    Page<Contact> searchByName(String name, int size, int page, String sortBy, String order, User user);

    Page<Contact> searchByEmail(String email, int size, int page, String sortBy, String order, User user);

    Page<Contact> searchByPhone(String phone, int size, int page, String sortBy, String order, User user);

    // get contacts by userId
    List<Contact> getByUserId(String userId);

    Page<Contact> getByUser(User user, int page, int size, String sortField, String sortDir);

    long countContacts(String userId);

}
