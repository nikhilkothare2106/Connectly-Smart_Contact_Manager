package com.scm.SCMProject.service;

import com.scm.SCMProject.entity.User;
import com.scm.SCMProject.forms.EditForm;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User saveUser(User user);

    User saveVerifiedUser(User user);

    Optional<User> getUserById(String id);

    Optional<User> updateUser(User user);

    void deleteUser(String id);

    boolean isUserExist(String userId);

    boolean isUserExistByEmail(String email);

    List<User> getAllUsers();

    User getUserByEmail(String email);

    User getUserByVerificationToken(String token);

     User updateUser(EditForm editForm);

}
