package com.nexgrid.cgsg.admin.security;

import com.nexgrid.cgsg.admin.service.LoginService;
import com.nexgrid.cgsg.admin.vo.LoginInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class AuthenticationConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService customAuthenticationProvider;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        csrfTokenRepository.setCookiePath("/");

        http.csrf()
                .csrfTokenRepository(csrfTokenRepository)
                .ignoringAntMatchers("/front/constant")
                .and()
//                .disable()
                .authorizeRequests()
                //.anyRequest().authenticated()
                .antMatchers("/login/**").permitAll()
                .and().formLogin().successHandler(this.getAuthenticationSuccessHandler())
        .failureHandler(this.geAuthenticationFailureHandler())
        ;
    }

    private AuthenticationSuccessHandler getAuthenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                response.getWriter().append("login success!!!");
            }
        };
    }

    private AuthenticationFailureHandler geAuthenticationFailureHandler() {
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                response.getWriter().append("login Fail >..<");
            }
        };
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(customAuthenticationProvider);
        auth.
                userDetailsService(customAuthenticationProvider);
//                .inMemoryAuthentication().withUser("test").password("{noop}test").roles("USER");

    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Service
    public static class CustomAuthenticationProvider implements UserDetailsService {

        @Autowired
        private LoginService loginService;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

            List<LoginInfo> loginInfo2 = loginService.getLoginInfo2(LoginInfo.builder().mbrId(username).build());

            if (loginInfo2 == null || loginInfo2.size() == 0) throw new UsernameNotFoundException("Username Not Found");

            List<GrantedAuthority> authorityList = new ArrayList<>();
            authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));

            LoginInfo loginInfo = loginInfo2.get(0);
            return new User(loginInfo.getMbrId(), loginInfo.getMbrPw(), authorityList);
        }
    }


}
