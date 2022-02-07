package com.example.studentmanagementsystem.controller;

import com.example.studentmanagementsystem.entity.Student;
import com.example.studentmanagementsystem.entity.User;
import com.example.studentmanagementsystem.service.StudentService;
import com.example.studentmanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/std")
public class StdController {

    @Autowired
    private StudentService service;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
//    pagination handler

    @GetMapping("students/{pageNo}")
    public String findPaginated(@PathVariable int pageNo, Model model, Principal principal) {

        model.addAttribute("std", "/std");
        model.addAttribute("toggle", "on");
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        String profileName = user.getA1firstName() + " " + user.getA2lastName();
        model.addAttribute("profileName", profileName);

        int pageSize = 10;
        Page<Student> page = service.findPaginated(pageNo, pageSize);
        List<Student> students = page.getContent();
        model.addAttribute("page2", "page");
        model.addAttribute("title", "Student login");
        model.addAttribute("CurrentPage", pageNo);
        model.addAttribute("totalPage", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());
        model.addAttribute("students", students);
        return "std";

    }


//    search handler

    @GetMapping("/students/search")
    public String getStudentsByName(@RequestParam(value = "firstName") String firstName, @RequestParam(value = "lastName", required = false) String lastName, Model model, Principal principal) {
        model.addAttribute("page2", "page");
        model.addAttribute("title", "Student login");
        model.addAttribute("toggle", "on");
        model.addAttribute("std", "/std");

        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        String profileName = user.getA1firstName() + " " + user.getA2lastName();
        model.addAttribute("profileName", profileName);

        List<Student> students = service.findByFirstNameAndLastName(firstName, lastName);
        model.addAttribute("students", students);


        return "std";
    }

    //    change password and email Handler

    @GetMapping("/students/setting")
    public String setting(Model model, Principal principal) {
        model.addAttribute("page2", "page");
        model.addAttribute("title", "Setting");
        model.addAttribute("toggle", "on");
        model.addAttribute("std", "/std");

        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        String profileName = user.getA1firstName() + " " + user.getA2lastName();
        model.addAttribute("profileName", profileName);
        return "setting";

    }


    @PostMapping("/students/setting/passwordCheck")
    public String changePassword(@RequestParam(name = "oldPassword") String oldPassword,
                                 @RequestParam(name = "password") String newPassword,
                                 @RequestParam(name = "confirmPassword") String confirmPassword,
                                 Principal principal, Model model) {

        model.addAttribute("page2", "page");
        model.addAttribute("title", "Setting");
        model.addAttribute("toggle", "on");
        model.addAttribute("std", "/std");


        int count = 1;
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);

        String profileName = user.getA1firstName() + " " + user.getA2lastName();
        model.addAttribute("profileName", profileName);


        if (!passwordEncoder.matches(oldPassword, user.getC2_password())) {
            model.addAttribute("notMatchedOld", "not matching old password");
            count++;
        }
        if (!(newPassword.toCharArray().length > 4)) {
            model.addAttribute("smallPass", "password is less than four char");
            count++;
        }
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("notMatchingNew", "not matching confirm password");
            count++;
        }
        if (count > 1) {

            model.addAttribute("fail", "password");

            return "setting";
        } else {
            model.addAttribute("success", "password");
            user.setC2_password(passwordEncoder.encode(newPassword));
            userService.insertUser(user);
            return "setting";
        }

    }

    @PostMapping("/students/setting/emailCheck")
    public String changeEmail(@RequestParam("email") String email, Principal principal, Model model) {
        model.addAttribute("page2", "page");
        model.addAttribute("title", "Setting");
        model.addAttribute("toggle", "on");
        model.addAttribute("std", "/std");


        int counter = 1;


        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);

        String profileName = user.getA1firstName() + " " + user.getA2lastName();
        model.addAttribute("profileName", profileName);
        List<String> errorMsg = new ArrayList<>();


        Pattern p = Pattern.compile("^[a-zA-Z0-9+_.-]{4,64}@[a-zA-Z0-9]+\\.[a-zA-Z0-9.-]{2,5}$");

        if (email.toCharArray().length < 1) {
            errorMsg.add("email is required!!");
            counter++;
        } else if (!p.matcher(email).find()) {
            errorMsg.add("invalid email!!");

            counter++;
        }


        if (counter > 1) {
            model.addAttribute("fail", "email");
            model.addAttribute("errorMsg", errorMsg);
            return "setting";

        } else {
            model.addAttribute("success", "email");
            user.setA3_email(email);
            userService.insertUser(user);
            return "setting";

        }

    }

    //    profile handler

    @GetMapping("/students/profile")
    public String getProfile(Model model, Principal principal) {

        model.addAttribute("page2", "page");
        model.addAttribute("title", "Profile");
        model.addAttribute("toggle", "on");
        model.addAttribute("std", "/std");


        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);

        String profileName = user.getA1firstName() + " " + user.getA2lastName();
        model.addAttribute("profileName", profileName);

        return "profile";
    }

    @GetMapping("/students/profile/edit")
    public String editProfile(Model model, Principal principal) {

        model.addAttribute("page2", "page");
        model.addAttribute("title", "Profile");
        model.addAttribute("toggle", "on");
        model.addAttribute("std", "/std");


        String username = principal.getName();
        User user = userService.getUserByUsername(username);


        String profileName = user.getA1firstName() + " " + user.getA2lastName();
        model.addAttribute("firstName", user.getA1firstName());
        model.addAttribute("lastName", user.getA2lastName());
        model.addAttribute("user", user);
        model.addAttribute("profileName", profileName);

        return "edit";
    }

    @PostMapping("/students/profile/edit")
    public String setProfile(Model model, Principal principal, @RequestParam(name = "firstName") String firstName, @RequestParam(name = "lastName") String lastName, @RequestParam(value = "profileImage", required = false) MultipartFile file) {

        model.addAttribute("page2", "page");
        model.addAttribute("title", "Profile");
        model.addAttribute("toggle", "on");
        model.addAttribute("std", "/std");

        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);

        String profileName = user.getA1firstName() + " " + user.getA2lastName();
        model.addAttribute("profileName", profileName);

        int counter = 1;
        String imageReference = null;


        if (file == null) {
            System.out.println("file is null");
        } else {
            if (file.isEmpty()) {
//            setting previous image if not getting any image

                if (user.getImage() != null) {
                    imageReference = user.getImage();
                }
            } else {
                try {
//                getting file in which we want to save image
                    File savingFile = new ClassPathResource("static/image").getFile();
//                creating unique name by appending id in file name
                    String[] img = file.getOriginalFilename().split("\\.");

                    imageReference = img[0] + user.getId() + "." + img[1];


//                creating path for the image to paste
                    Path path = Paths.get(savingFile.getAbsolutePath() + File.separator + imageReference);

//                copy file from input stream(bytes code) and paste it into defined path
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (firstName.toCharArray().length < 1) {
            model.addAttribute("error", "first name is required!!");
            counter++;
        }

        if (lastName.toCharArray().length < 1) {
            model.addAttribute("error1", "last name is required!!");
            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName", lastName);
            counter++;
        }

        if (counter > 1) {

            return "edit";
        } else {
            user.setImage(imageReference);
            user.setA1firstName(firstName);
            user.setA2lastName(lastName);
            userService.insertUser(user);
            return "redirect:/std/students/profile";
        }
    }

}
