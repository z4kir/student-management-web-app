package com.example.studentmanagementsystem.repository;

import com.example.studentmanagementsystem.entity.Student;
import com.example.studentmanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByFirstNameContaining(String firstName);

    List<Student> findByFirstNameContainingAndLastNameContaining(String firstName, String lastName);

    List<Student> findByFirstNameContainingAndUser(String firstName, User user);

    List<Student> findByFirstNameContainingAndLastNameContainingAndUser(String firstName, String lastName, User user);

    List<Student> findByRollNoAndUser(String rollNo, User user);

}
