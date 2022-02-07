package com.example.studentmanagementsystem.controller;

import com.example.studentmanagementsystem.entity.Student;
import com.example.studentmanagementsystem.entity.User;
import com.example.studentmanagementsystem.service.StudentService;
import com.example.studentmanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SearchController {

    @Autowired
    StudentService studentService;

    @Autowired
    UserService userService;

    //method for not showing repeated students who have same first name and last name

    private List<Student> checkStudentForDuplicate(List<Student> students) {
        List<String> lastNameCheckingForDuplicate = new ArrayList<String>();
        List<String> firstNameCheckingForDuplicate = new ArrayList<String>();
        for (Student student : students) {
            lastNameCheckingForDuplicate.add(student.getLastName());
            firstNameCheckingForDuplicate.add(student.getFirstName());
        }


        if (lastNameCheckingForDuplicate.size() > 1) {
            for (int i = 0; i < lastNameCheckingForDuplicate.size() - 1; i++) {

                for (int j = i + 1; j < lastNameCheckingForDuplicate.size(); j++) {
                    if (lastNameCheckingForDuplicate.get(i).equals(lastNameCheckingForDuplicate.get(j)) && firstNameCheckingForDuplicate.get(i).equals(firstNameCheckingForDuplicate.get(j))) {
                        students.remove(j);
                        lastNameCheckingForDuplicate.remove(j);
                        j--;
                    }
                }

            }
        }
        return students;
    }


//    1) For Students search

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(value = "firstName") String firstName, @RequestParam(value = "lastName", required = false) String lastName) {


//        choosing method according to given URL condition (only First Name or First Name and Last Name)


//        1.only FirstName

        if (lastName == null) {

            List<Student> students = studentService.findByFirstName(firstName);
            List<Student> studentList = checkStudentForDuplicate(students);
            return ResponseEntity.ok(studentList);
        }

//        2.FirstName and LastName

        else {
            List<Student> students = studentService.findByFirstNameAndLastName(firstName, lastName);
            List<Student> studentList = checkStudentForDuplicate(students);
            return ResponseEntity.ok(studentList);
        }

    }

//    2) For Teachers Search

    @GetMapping("teacher/search")
    public ResponseEntity<?> teacherSearch(@RequestParam(value = "firstName") String firstName, @RequestParam(value = "lastName", required = false) String lastName, Principal principal) {

        String username = principal.getName();
        User user = userService.getUserByUsername(username);

        //        choosing method according to given URL condition (only First Name or First Name and Last Name)


        //        1.only FirstName

        if (lastName == null) {

            //Loop for not showing repeated students who have same first name and last name

            List<Student> students = studentService.findByFirstNameFromGivenUser(firstName, user);
            List<Student> studentList = checkStudentForDuplicate(students);
            return ResponseEntity.ok(studentList);
        }

//        2.FirstName and LastName

        else {
            List<Student> students = studentService.findByFirstNameAndLastNameFromGivenUser(firstName, lastName, user);
            List<Student> studentList = checkStudentForDuplicate(students);
            return ResponseEntity.ok(studentList);
        }
    }


//    3) For StudentID's Search

    //method for not showing repeated users who have same first name and last name

    private List<User> checkUserForDuplicate(List<User> users) {
        List<String> lastNameCheckingForDuplicate = new ArrayList<String>();
        List<String> firstNameCheckingForDuplicate = new ArrayList<String>();
        for (User user : users) {
            lastNameCheckingForDuplicate.add(user.getA2lastName());
            firstNameCheckingForDuplicate.add(user.getA1firstName());
        }


        if (lastNameCheckingForDuplicate.size() > 1) {
            for (int i = 0; i < lastNameCheckingForDuplicate.size() - 1; i++) {

                for (int j = i + 1; j < lastNameCheckingForDuplicate.size(); j++) {

                    if (lastNameCheckingForDuplicate.get(i).equals(lastNameCheckingForDuplicate.get(j)) && firstNameCheckingForDuplicate.get(i).equals(firstNameCheckingForDuplicate.get(j))) {
                        users.remove(j);
                        lastNameCheckingForDuplicate.remove(j);
                        j--;
                    }
                }

            }
        }
        return users;
    }


    @GetMapping("studentID/search")
    public ResponseEntity<?> StudentIDSearch(@RequestParam(value = "firstName") String firstName, @RequestParam(value = "lastName", required = false) String lastName) {

        //        choosing method according to given URL condition (only First Name or First Name and Last Name)


        //        1.only FirstName

        if (lastName == null) {
            List<User> users = userService.findByFirstName(firstName);
            List<User> userList = checkUserForDuplicate(users);
            return ResponseEntity.ok(userList);
        }

//        2.FirstName and LastName

        else {
            List<User> users = userService.findByFirstNameAndLastName(firstName, lastName);
            List<User> userList = checkUserForDuplicate(users);
            return ResponseEntity.ok(userList);
        }
    }


//4.Id search

    @GetMapping("idList/search")
    public ResponseEntity<?> IDSearch(@RequestParam(value = "firstName") String firstName, @RequestParam(value = "lastName", required = false) String lastName) {

        //        choosing method according to given URL condition (only First Name or First Name and Last Name)


        //        1.only FirstName

        if (lastName == null) {

            List<User> users = userService.findAllByFirstName(firstName);
            List<User> userList = checkUserForDuplicate(users);
            return ResponseEntity.ok(userList);
        }

//        2.FirstName and LastName

        else {
            List<User> users = userService.findAllByFirstNameAndLastName(firstName, lastName);
            List<User> userList = checkUserForDuplicate(users);
            return ResponseEntity.ok(userList);
        }
    }
}



