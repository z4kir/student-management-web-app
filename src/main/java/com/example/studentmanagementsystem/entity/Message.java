package com.example.studentmanagementsystem.entity;

import lombok.Data;
import org.springframework.context.annotation.Bean;

@Data
public class Message {
    private String content;
    private String type;

}
