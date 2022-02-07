package com.example.studentmanagementsystem.controller;

import com.example.studentmanagementsystem.entity.User;
import com.example.studentmanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
@RequestMapping("/admin/zakir")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    private Model commonAttributes(Model model, String title) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("admin", "/admin");
        model.addAttribute("page2", "page");
        model.addAttribute("toggle", "on");
        model.addAttribute("title", title);
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        String profileName = user.getA1firstName() + " " + user.getA2lastName();
        model.addAttribute("profileName", profileName);
        return model;
    }

    @GetMapping("/idList/{pageNo}")
    public String getAllIDs(@PathVariable int pageNo, Model model) {
        int pageSize = 6;

        model = commonAttributes(model, "ID List");
        Page<User> userPage = userService.findALlExceptAdmin(pageNo, pageSize);
        List<User> userList = userPage.getContent();
        model.addAttribute("CurrentPage", pageNo);
        model.addAttribute("totalUsers", userPage.getTotalElements());
        model.addAttribute("totalPage", userPage.getTotalPages());
        model.addAttribute("students", userList);
        return "idList";
    }

    @GetMapping("/delete/{id}")
    public String deleteID(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/zakir/idList/1";
    }

    @GetMapping("/idList/search")
    public String getIDSearch(@RequestParam(value = "firstName") String firstName, @RequestParam(value = "lastName", required = false) String lastName, Model model) {
        model = commonAttributes(model, "Search ID's");

        List<User> users = userService.findAllByFirstNameAndLastName(firstName, lastName);
        model.addAttribute("students", users);
        return "idList";
    }

    @GetMapping("/setting")
    public String setting(Model model) {
        model = commonAttributes(model, "Setting");
        return "setting";

    }


    @PostMapping("/setting/passwordCheck")
    public String changePassword(@RequestParam(name = "oldPassword") String oldPassword,
                                 @RequestParam(name = "password") String newPassword,
                                 @RequestParam(name = "confirmPassword") String confirmPassword,
                                 Principal principal, Model model) {

        model = commonAttributes(model, "Setting");


        int count = 1;
        String username = principal.getName();
        User user = userService.getUserByUsername(username);


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

    @PostMapping("/setting/emailCheck")
    public String changeEmail(@RequestParam("email") String email, Principal principal, Model model) {


        model = commonAttributes(model, "Setting");

        int counter = 1;


        String username = principal.getName();
        User user = userService.getUserByUsername(username);


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

    @GetMapping("/profile")
    public String getProfile(Model model, Principal principal) {

        model = commonAttributes(model, "Profile");

        return "profile";
    }

    @GetMapping("/profile/edit")
    public String editProfile(Model model, Principal principal) {

        model = commonAttributes(model, "Profile");


        String username = principal.getName();
        User user = userService.getUserByUsername(username);


        model.addAttribute("firstName", user.getA1firstName());
        model.addAttribute("lastName", user.getA2lastName());


        return "edit";
    }

    @PostMapping("/profile/edit")
    public String setProfile(Model model, Principal principal, @RequestParam(name = "firstName") String firstName, @RequestParam(name = "lastName") String lastName, @RequestParam(value = "profileImage", required = false) MultipartFile file) {

        model = commonAttributes(model, "Setting");

        String username = principal.getName();
        User user = userService.getUserByUsername(username);


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
            return "redirect:/admin/zakir/profile";
        }
    }
}
