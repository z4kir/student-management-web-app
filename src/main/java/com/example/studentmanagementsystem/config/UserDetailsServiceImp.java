package com.example.studentmanagementsystem.config;

import com.example.studentmanagementsystem.entity.User;
import com.example.studentmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        User user = userRepository.getUserByUsername(s);
        if (user == null) {
            throw new UsernameNotFoundException("could not found user !!");
        }

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        return customUserDetails;

    }
}
