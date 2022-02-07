package com.example.studentmanagementsystem.entity;

import com.example.studentmanagementsystem.validation.ConfirmPassword;
import com.example.studentmanagementsystem.validation.Unique;
import com.example.studentmanagementsystem.validation.ValidPassword;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@ConfirmPassword(first = "c2_password", second = "confirmPassword")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstname")
    @NotBlank(message = "First Name is required!!")
    private String a1firstName;

    @Column(name = "lastname")
    @NotBlank(message = "Last Name is required!!")
    private String a2lastName;

    @Column(name = "e_mail")
    @Email(message = "Please enter correct email!!")
    @NotBlank(message = "Email is required!!")
    private String a3_email;

    @NotBlank(message = "username is required !!")
    @Size(min = 2, max = 20, message = "At least 2 and maximum 20 characters required !!")
    @Column(name = "username", nullable = false, unique = true)
    @Unique(message = "username is already given please enter different username !!", field = "username")
    private String c1_username;

    @ValidPassword
    @Column(name = "password", nullable = false)
    private String c2_password;

    @Column(name = "date", nullable = false)
    private String c3_date;

    @Column(name = "time", nullable = false)
    private String c4_time;

    @Column(name = "role", nullable = false)
    private String c5role;

    @Column(name = "image")
    private String image;

    @Transient
    private String confirmPassword;


    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    List<Student> students = new ArrayList<>();

    @PrePersist
    public void insertDateTimeRole() {
        LocalDate d = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        c3_date = d.format(dateFormatter);


        LocalTime t = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        c4_time = t.format(timeFormatter);

    }


}
