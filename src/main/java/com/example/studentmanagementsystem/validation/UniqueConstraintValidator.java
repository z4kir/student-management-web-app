package com.example.studentmanagementsystem.validation;

import com.example.studentmanagementsystem.entity.Student;
import com.example.studentmanagementsystem.entity.User;
import com.example.studentmanagementsystem.repository.UserRepository;
import com.example.studentmanagementsystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueConstraintValidator implements ConstraintValidator<Unique, String> {

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserRepository userService;

    @Autowired
    private HttpSession session;

    private String fieldName;


    @Override
    public void initialize(Unique constraintAnnotation) {
        fieldName = constraintAnnotation.field();

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        boolean unique;

        if (fieldName.equals("rollNo")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.getUserByUsername(username);

            if (!(session.getAttribute("id") == null)) {
                Long id = (Long) session.getAttribute("id");
                Student existStudent = studentService.getById(id);
                unique = studentService.findByRollNoFromGivenUser(s, user).isEmpty() || existStudent.getRollNo().equals(s);
            } else {

                unique = studentService.findByRollNoFromGivenUser(s, user).isEmpty();
            }

            return unique;
        } else {
            User existUser = userService.getUserByUsername(s);
            if (existUser == null) {
                unique = true;
            } else {
                unique = false;
            }
            return unique;
        }

    }

}
