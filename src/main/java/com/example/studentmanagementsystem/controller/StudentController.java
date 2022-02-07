package com.example.studentmanagementsystem.controller;

import com.example.studentmanagementsystem.entity.Student;
import com.example.studentmanagementsystem.entity.User;
import com.example.studentmanagementsystem.service.StudentService;
import com.example.studentmanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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
@RequestMapping("/user")
public class StudentController {

    @Autowired
    private UserService userService;

    private StudentService service;


    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    public StudentController(StudentService service) {
        this.service = service;
    }


    //    handler method to get all the students stored in database
    @GetMapping("/students")
    public String listStudents(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        List<Student> students = user.getStudents();
        model.addAttribute("students", students);
        model.addAttribute("ScrollBarLimitForTable", students.size());
        model.addAttribute("title", "Student Management");
        model.addAttribute("page2", "page");
        model.addAttribute("toggle", "on");
        model.addAttribute("user", user);
        String profileName = user.getA1firstName() + " " + user.getA2lastName();
        model.addAttribute("profileName", profileName);


        return "students";

    }


    @GetMapping("/students/new")
    public String addStudent(Model model, Principal principal, HttpSession session) {

        session.removeAttribute("id");

        Student student = new Student();
        model.addAttribute("student", student);
        model.addAttribute("title", "Add New Student");
        model.addAttribute("page2", "page");
        model.addAttribute("toggle", "on");

        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);

        String profileName = user.getA1firstName() + " " + user.getA2lastName();
        model.addAttribute("profileName", profileName);


        return "add_student";
    }

    @PostMapping("/students/new")
    public String saveStudent(@Valid @ModelAttribute("student") Student student, BindingResult result, Principal principal, Model model) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        String profileName = user.getA1firstName() + " " + user.getA2lastName();
        model.addAttribute("profileName", profileName);
        model.addAttribute("user", user);
        if (result.hasErrors()) {
            model.addAttribute("student", student);
            model.addAttribute("title", "Add New Student");
            model.addAttribute("page2", "page");
            model.addAttribute("toggle", "on");
            return "add_student";
        }

        service.setId(student);

        student.setUser(user);
        service.addStudent(student);

        return "redirect:/user/students";
    }

    @GetMapping("/students/update/{id}")
    public String getStudentForUpdate(@PathVariable("id") Long id, Model model, Principal principal, HttpSession session) {
        model.addAttribute("student", service.getById(id));
        model.addAttribute("title", "Update Student");
        model.addAttribute("toggle", "on");
        model.addAttribute("page2", "page");

        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);

        String profileName = user.getA1firstName() + " " + user.getA2lastName();
        model.addAttribute("profileName", profileName);

        session.setAttribute("id", id);


        return "update_student";
    }

    @PostMapping("/students/{id}")
    public String updateStudent(@PathVariable("id") Long id,
                                @Valid @ModelAttribute("student") Student student,
                                BindingResult result, Principal principal, Model model) {


        if (result.hasErrors()) {
            model.addAttribute("student", student);
            model.addAttribute("title", "Update Student");
            model.addAttribute("toggle", "on");

            String username = principal.getName();
            User user = userService.getUserByUsername(username);
            model.addAttribute("user", user);

            String profileName = user.getA1firstName() + " " + user.getA2lastName();
            model.addAttribute("profileName", profileName);

            model.addAttribute("page2", "page");
            return "update_student";
        }
        Student existStudent = service.getById(id);
        existStudent.setId(id);
        existStudent.setRollNo(student.getRollNo());
        existStudent.setFirstName(student.getFirstName());
        existStudent.setLastName(student.getLastName());
        existStudent.setEmail(student.getEmail());


        service.updateStudent(existStudent);
        return "redirect:/user/students";
    }

    @GetMapping("/students/{id}")
    public String deleteStudent(@PathVariable Long id,
                                @ModelAttribute("student") Student student) {
        service.deleteStudent(id);

        return "redirect:/user/students";

    }


    //    search handler
    @GetMapping("/students/search")
    public String getStudentsByName(@RequestParam(value = "firstName") String firstName, @RequestParam(value = "lastName", required = false) String lastName, Model model, Principal principal) {
        model.addAttribute("page2", "page");
        model.addAttribute("title", "Student Management");
        model.addAttribute("toggle", "on");
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);

        String profileName = user.getA1firstName() + " " + user.getA2lastName();
        model.addAttribute("profileName", profileName);

        List<Student> students = service.findByFirstNameAndLastNameFromGivenUser(firstName, lastName, user);
        model.addAttribute("students", students);


        return "students";
    }

//    change password and email Handler

    @GetMapping("/students/setting")
    public String setting(Model model, Principal principal) {
        model.addAttribute("page2", "page");
        model.addAttribute("title", "Setting");
        model.addAttribute("toggle", "on");

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

        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);

        String profileName = user.getA1firstName() + " " + user.getA2lastName();
        model.addAttribute("profileName", profileName);

        int counter = 1;

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
            model.addAttribute("email", email);
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

        String username = principal.getName();
        User user = userService.getUserByUsername(username);


        String profileName = user.getA1firstName() + " " + user.getA2lastName();
        model.addAttribute("user", user);
        model.addAttribute("profileName", profileName);

        return "profile";
    }

    @GetMapping("/students/profile/edit")
    public String editProfile(Model model, Principal principal) {

        model.addAttribute("page2", "page");
        model.addAttribute("title", "Profile");
        model.addAttribute("toggle", "on");

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
            return "redirect:/user/students/profile";
        }
    }


}









