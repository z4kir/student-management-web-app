package com.example.studentmanagementsystem.entity;

import com.example.studentmanagementsystem.validation.Unique;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Entity
@Table(name = "student")
public class Student {

    @Id
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Column(name = "Roll_no", nullable = false, unique = false)
    @NotBlank(message = "roll no. is required")
    @Pattern(regexp = "(^([0-9]{3}))(([1-9])$)", message = "roll no. should  contains 4 numbers and last digit should not be zero Example: 0001")
    @Unique(message = "This Roll No is already Given", field = "rollNo")
    private String rollNo;

    @Column(name = "first_name")
    @NotBlank(message = "First name is required!!")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "last name is required!!")
    private String lastName;

    @Column(name = "e_mail")
    @Email(message = "Please enter correct email!!")
    @NotBlank(message = "Email is required!!")
    private String email;

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
