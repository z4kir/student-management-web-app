package com.example.studentmanagementsystem.controller;

import com.example.studentmanagementsystem.entity.Message;
import com.example.studentmanagementsystem.entity.User;
import com.example.studentmanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;


@Controller
public class UserController {

    @Autowired
    private Message msg;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //    Rest API for User entity

    @PostMapping("/register")
    public String insertUser(@Valid @ModelAttribute("user") User user, BindingResult result, @RequestParam(name = "select") String choose, Model model, HttpSession session) {

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("title", "SignUp Student Management");
            model.addAttribute("page3", "page");
            return "signup";
        }

        if (choose.equals("1")) {
            user.setC5role("ROLE_STUDENT");
        } else if (choose.equals("")) {
            user.setC5role("ROLE_TEACHER");
        }
        user.setC2_password(passwordEncoder.encode(user.getC2_password()));
        user.setConfirmPassword(user.getC2_password());
        userService.insertUser(user);
        return "redirect:/signIn";

    }

    @GetMapping("/user/students/studentID")
    public String getStudentID(Model model, Principal principal) {
        List<User> users = userService.getAllByRole("ROLE_STUDENT");
        model.addAttribute("students", users);
        model.addAttribute("ScrollBar", users.size());
        model.addAttribute("title", "Student ID's List");
        model.addAttribute("page2", "page");
        model.addAttribute("toggle", "on");
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        return "studentID";
    }

    @GetMapping("/user/students/studentID/{id}")
    public String deleteStudentID(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/user/students/studentID";
    }

    //    search handler
    @GetMapping("/user/students/studentID/search")
    public String getStudentIDSearch(@RequestParam(value = "firstName") String firstName, @RequestParam(value = "lastName", required = false) String lastName, Model model, Principal principal) {
        model.addAttribute("title", "Student ID's List");
        model.addAttribute("page2", "page");
        model.addAttribute("toggle", "on");

        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);

        List<User> users = userService.findByFirstNameAndLastName(firstName, lastName);
        model.addAttribute("students", users);
        return "studentID";
    }

}