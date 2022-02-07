package com.example.studentmanagementsystem.service.imp;

import com.example.studentmanagementsystem.entity.User;
import com.example.studentmanagementsystem.repository.UserRepository;
import com.example.studentmanagementsystem.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImp implements UserService {
    UserRepository repository;

    public UserServiceImp(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User insertUser(User user) {
        return repository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public List<User> getAllByRole(String role) {
        return repository.getAllByC5_role(role);
    }

    @Override
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return repository.getUserByUsername(username);
    }

    @Override
    public List<User> findByFirstName(String firstName) {
        return repository.findByA1firstNameContainingAndC5role(firstName, "ROLE_STUDENT");
    }

    @Override
    public List<User> findByFirstNameAndLastName(String firstName, String lastName) {
        return repository.findByA1firstNameContainingAndA2lastNameContainingAndC5role(firstName, lastName, "ROLE_STUDENT");
    }

    @Override
    public Page<User> findALlExceptAdmin(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return repository.findByC5roleNot("ROLE_ADMIN", pageable);
    }


    @Override
    public List<User> findAllByFirstName(String firstName) {
        return repository.findByA1firstNameContainingAndC5roleNot(firstName, "ROLE_ADMIN");
    }

    @Override
    public List<User> findAllByFirstNameAndLastName(String firstName, String lastName) {
        return repository.findByA1firstNameContainingAndA2lastNameContainingAndC5roleIsNot(firstName, lastName, "ROLE_ADMIN");
    }

//    verifying email


    @Override
    public User getByEmail(String email) {
        return repository.findByA3_email(email);
    }
}
