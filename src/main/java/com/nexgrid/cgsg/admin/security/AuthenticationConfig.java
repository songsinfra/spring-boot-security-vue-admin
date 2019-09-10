package com.nexgrid.cgsg.admin.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexgrid.cgsg.admin.constants.SystemStatusCode;
import com.nexgrid.cgsg.admin.exception.AdminException;
import com.nexgrid.cgsg.admin.service.LoginService;
import com.nexgrid.cgsg.admin.vo.LoginInfo;
import com.nexgrid.cgsg.admin.vo.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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
    private CustomDaoAuthenticationProvider customDaoAuthenticationProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LoginService loginService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        csrfTokenRepository.setCookiePath("/");

        http.csrf()
                .csrfTokenRepository(csrfTokenRepository)
                .ignoringAntMatchers("/front/constant")
                .ignoringAntMatchers("/login/**")
                .and()
//                .disable()
                .authorizeRequests()
                //.anyRequest().authenticated()
                .antMatchers("/login/**").permitAll()
                .and().formLogin().successHandler(this.getAuthenticationSuccessHandler())
                .failureHandler(this.geAuthenticationFailureHandler())
                .and()
                .logout()
                .logoutSuccessHandler(this.getLogoutSuccessHandler())
        ;
    }

    private AuthenticationSuccessHandler getAuthenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(SystemStatusCode.LOGIN_SUCCESS.getCode());

                response.setStatus(HttpStatus.OK.value());
                response.getWriter().append(objectMapper.writeValueAsString(resultInfo));
            }
        };
    }

    private AuthenticationFailureHandler geAuthenticationFailureHandler() {
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                ResultInfo resultInfo = new ResultInfo();

                if (exception instanceof AdminException) {
                    AdminException e = (AdminException) exception;
                    resultInfo.setCode(e.getCode().getCode());
                    resultInfo.setMsg(e.getMessage());
                } else if (exception instanceof BadCredentialsException) {
                    resultInfo.setCode(SystemStatusCode.FAIL_LOGIN.getCode());
                    resultInfo.setMsg(exception.getMessage());
                }

                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.getWriter().append(objectMapper.writeValueAsString(resultInfo));
            }
        };
    }

    private LogoutSuccessHandler getLogoutSuccessHandler() {
        return new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setMsg(SystemStatusCode.LOGIN_SUCCESS.getCode());
                response.getWriter().append(objectMapper.writeValueAsString(resultInfo));
            }
        };
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customDaoAuthenticationProvider);
    }

    @Service
    public static class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {
        @Autowired
        private CustomUserDetailsService customUserDetailsService;

        @Autowired
        private LoginService loginService;

        @Override
        protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
            LoginInfo loginInfo = null;
            try {
                loginInfo = ((AdminUser) userDetails).getLoginInfo();
                super.additionalAuthenticationChecks(userDetails, authentication);

                loginService.successLogin(loginInfo);
            } catch (AuthenticationException e) {
                if(e instanceof BadCredentialsException){
                    loginService.failLogin(loginInfo);
                } else{
                    throw e;
                }
            }
        }

        @PostConstruct
        public void init() {
            this.setUserDetailsService(customUserDetailsService);
        }
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Service
    public static class CustomUserDetailsService implements UserDetailsService {

        @Autowired
        private LoginService loginService;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            List<LoginInfo> loginInfo2 = loginService.getLoginInfo2(LoginInfo.builder().mbrId(username).build());

            if (loginInfo2 == null || loginInfo2.size() == 0) throw new AdminException(SystemStatusCode.NOT_FOUND_USER_ID, "사용자 아이디 없음"); //throw new UsernameNotFoundException("Username Not Found");

            List<GrantedAuthority> authorityList = new ArrayList<>();
            authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));

            LoginInfo loginInfo = loginInfo2.get(0);
            return new AdminUser(loginInfo.getMbrId(), loginInfo.getMbrPw(), authorityList, loginInfo);
        }
    }

}
