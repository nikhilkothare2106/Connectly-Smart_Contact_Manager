package com.scm.SCMProject.Repository;

import com.scm.SCMProject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailToken(String token);
}