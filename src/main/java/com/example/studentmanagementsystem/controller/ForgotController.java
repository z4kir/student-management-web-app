package com.example.studentmanagementsystem.controller;

import com.example.studentmanagementsystem.entity.User;
import com.example.studentmanagementsystem.service.EmailService;
import com.example.studentmanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Random;

@Controller
public class ForgotController {

    private Random random = new Random();

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/forgot")
    public String forgot(Model model, HttpSession session) {
        model.addAttribute("title", "Forget Password");
        session.removeAttribute("msg");
        return "forgot";
    }

    @PostMapping("/sendOTP")
    public String sendOTP(@RequestParam(name = "email", required = false) String email, Model model, HttpSession session) {
        model.addAttribute("title", "Verify OTP");
        String to;

        String otp = String.format("%04d", random.nextInt(10000));
        if (email == null) {
            String email1 = session.getAttribute("email").toString();
            System.out.println("Email :" + email1);
            to = email1;
        } else {
            session.setAttribute("email", email);
            System.out.println("Email :" + email);
            to = email;
        }

        System.out.println("Random :" + otp);

        String subject = "OTP from Student Management";
        String msg = ""
                + "<div style='border:1px solid #e2e2e2;padding:20px'>"
                + "<h1>"
                + "OTP is "
                + "<b>" + otp
                + "</n>"
                + "</h1>"
                + "</div>";

        boolean flag = emailService.sendMessage(subject, msg, to);

        if (flag) {
            session.setAttribute("otp", otp);
            session.removeAttribute("msg");
            session.setAttribute("success", "success");
            return "verify_otp";
        } else {

            session.setAttribute("msg", "Check your email id !!");
            return "forgot";

        }
    }

    @PostMapping("/verify-otp")
    public String verifyOTP(@RequestParam(name = "otp") String otp, Model model, HttpSession session) {

        String myotp = (String) session.getAttribute("otp");
        String email = (String) session.getAttribute("email");
        if (otp.equals(myotp)) {
            User user = userService.getByEmail(email);
            if (user == null) {
                session.setAttribute("msg", "Email is not registered !!");
                return "forgot";

            } else {

                return "change_password";
            }
        } else {
            session.setAttribute("msg", "Wrong OTP Please insert again");
            session.removeAttribute("success");
            return "verify_otp";
        }


    }

    @PostMapping("/change_password")
    public String changePassword(@RequestParam(name = "password") String newPassword,
                                 @RequestParam(name = "confirmPassword") String confirmPassword,
                                 HttpSession session, Model model) {

        model.addAttribute("title", "Change Password");


        int count = 1;
        String email = (String) session.getAttribute("email");
        User user = userService.getByEmail(email);


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

            return "change_password";
        } else {
            user.setC2_password(passwordEncoder.encode(newPassword));
            userService.insertUser(user);
            return "redirect:/signIn?changePassword";
        }

    }
}
