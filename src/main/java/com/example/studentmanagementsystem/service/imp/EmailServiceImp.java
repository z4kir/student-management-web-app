package com.example.studentmanagementsystem.service.imp;

import com.example.studentmanagementsystem.service.EmailService;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class EmailServiceImp implements EmailService {

    @Override
    public boolean sendMessage(String subject, String msg, String to) {

        String from = "studentmanagementservice@gmail.com";
        String password = "Zakir@1999";

////        Variable for gmail
//        String host="smtp.gmail.com";
//
////        get the system properties
//        Properties properties=System.getProperties();
//
////        setting important information in properties
//        properties.put("mail.smtp.host",host);
//        properties.put("mail.smtp.port","465");
//        properties.put("mail.smtp.ssl.enable","true");
//        properties.put("mail.smtp.auth","true");
//
////        Step 1:to get the session object
//        Session session=Session.getInstance(properties, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(from,password);
//            }
//
//        });
//        session.setDebug(true);
//
////        step 2:compose the message [text,multi media]
//        MimeMessage message=new MimeMessage(session);
//
//        try {
////        from email
//            message.setFrom(from);
//
////            adding recipient to message
//            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
//
////            adding subject
//            message.setSubject(subject);
//
////            adding message(Text)
//            message.setText(msg);
//            message.setContent(msg,"text/html");
//
////            step 3:send message by using transport class
//            Transport.send(message);
//            return true;
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//            return false;
//        }

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(from);
        mailSender.setPassword("idrxlvuhivefyafb");

        Properties mailProperties = new Properties();
        mailProperties.setProperty("mail.smtp.auth", "true");
        mailProperties.setProperty("mail.smtp.starttls.enable", "true");

        mailSender.setJavaMailProperties(mailProperties);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setFrom(from, "Student Management");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(msg, true);

            mailSender.send(mimeMessage);
            return true;
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }


    }
}
