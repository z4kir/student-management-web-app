package com.example.studentmanagementsystem.service;

import com.example.studentmanagementsystem.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    User insertUser(User user);

    List<User> getAllUsers();

    List<User> getAllByRole(String role);

    User getUserByUsername(String username);

    void deleteUser(Long id);

    List<User> findByFirstName(String firstName);

    List<User> findByFirstNameAndLastName(String firstName, String lastName);

    Page<User> findALlExceptAdmin(int pageNo, int pageSize);

    List<User> findAllByFirstName(String firstName);

    List<User> findAllByFirstNameAndLastName(String firstName, String lastName);

    User getByEmail(String email);
}
