package com.example.studentmanagementsystem.service;

import com.example.studentmanagementsystem.entity.Student;
import com.example.studentmanagementsystem.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();

    Student addStudent(Student student);

    Student getById(Long id);

    Student updateStudent(Student student);

    void deleteStudent(Long id);

    void setId(Student student);

    //    pagination of table
    Page<Student> findPaginated(int pageNo, int pageSize);


    //    search methods
    List<Student> findByFirstNameAndLastName(String firstName, String lastName);

    List<Student> findByFirstName(String firstName);

    List<Student> findByFirstNameFromGivenUser(String firstName, User user);

    List<Student> findByFirstNameAndLastNameFromGivenUser(String firstName, String lastName, User user);

    List<Student> findByRollNoFromGivenUser(String rollNo, User user);
}
