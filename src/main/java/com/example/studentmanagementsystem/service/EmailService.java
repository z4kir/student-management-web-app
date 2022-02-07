package com.example.studentmanagementsystem.service;

public interface EmailService {
    public boolean sendMessage(String subject, String msg, String to);
}
