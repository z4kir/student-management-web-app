package com.example.studentmanagementsystem.repository;

import com.example.studentmanagementsystem.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.c1_username=:username")
    User getUserByUsername(@Param("username") String username);

    @Query("select u from User u where u.c5role=:role")
    List<User> getAllByC5_role(@Param("role") String role);

    @Query("select u from User u where u.a3_email=:email")
    User findByA3_email(@Param("email") String email);

    List<User> findByA1firstNameContainingAndC5role(String firstName, String role);

    List<User> findByA1firstNameContainingAndA2lastNameContainingAndC5role(String firstName, String lastName, String role);

    Page<User> findByC5roleNot(String role1, Pageable pageable);

    List<User> findByA1firstNameContainingAndC5roleNot(String firstName, String role1);

    List<User> findByA1firstNameContainingAndA2lastNameContainingAndC5roleIsNot(String firstName, String lastName, String role);


}
