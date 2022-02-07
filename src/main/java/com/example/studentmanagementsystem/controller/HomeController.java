package com.example.studentmanagementsystem.controller;

import com.example.studentmanagementsystem.entity.User;
import com.example.studentmanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String getHome(Model model) {

        model.addAttribute("title", "Home Student Management");
        model.addAttribute("page", "page");
        return "home";
    }

    @GetMapping("/signIn")
    public String getLogIn(@ModelAttribute User user, Model model, Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();


        User user1 = userService.getUserByUsername(userName);
        model.addAttribute("page2", "page");
        model.addAttribute("title", "LogIn Student Management");
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        if (user1.getC5role().equals("ROLE_TEACHER")) {
            return "redirect:/user/students";
        }

        if (user1.getC5role().equals("ROLE_STUDENT")) {
            return "redirect:/std/students/1";
        }
        if (user1.getC5role().equals("ROLE_ADMIN")) {
            return "redirect:/admin/zakir/idList/1";
        }

        return "login";
    }

    @GetMapping("/register")
    public String getSignUp(@ModelAttribute User user, Model model, HttpSession session) {
        model.addAttribute("title", "SignUp Student Management");
        model.addAttribute("page3", "page");
        return "signup";
    }

}
