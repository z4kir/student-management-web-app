package com.example.studentmanagementsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Configuration
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        String redirectURl = null;

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {


            if (grantedAuthority.getAuthority().equals("ROLE_STUDENT")) {

                redirectURl = "/std/students/1";
                break;

            } else if (grantedAuthority.getAuthority().equals("ROLE_TEACHER")) {
                redirectURl = "/user/students";
                break;

            } else if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
                redirectURl = "/admin/zakir/idList/1";
            }
        }

        if (redirectURl == null) {
            throw new IllegalStateException();
        }


        redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, redirectURl);


    }
}
