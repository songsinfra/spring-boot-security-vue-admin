package com.nexgrid.cgsg.admin.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexgrid.cgsg.admin.constants.SystemStatusCode;
import com.nexgrid.cgsg.admin.exception.AdminException;
import com.nexgrid.cgsg.admin.service.LoginService;
import com.nexgrid.cgsg.admin.utils.CommonUtil;
import com.nexgrid.cgsg.admin.vo.LoginInfo;
import com.nexgrid.cgsg.admin.vo.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@Slf4j
public class AuthenticationConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    @Autowired
    private CustomDaoAuthenticationProvider customDaoAuthenticationProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LoginService loginService;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:3000");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/_nuxt/**",
                "/css/**",
                "/data/**",
                "/fonts/**",
                "/images/**",
                "/js/**",
                "/vendors/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        csrfTokenRepository.setCookiePath("/");

        http.csrf()
//                .csrfTokenRepository(csrfTokenRepository)
//                .ignoringAntMatchers("/front/constant")
//                .ignoringAntMatchers("/menu/**")
//                .ignoringAntMatchers("/login/**")
//                .and()
                .disable()
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/login/**").permitAll()
                    .antMatchers("/**").authenticated()
                .and()
                    .formLogin()
                        .successHandler(this.getAuthenticationSuccessHandler())
                        .failureHandler(this.geAuthenticationFailureHandler())
                .and()
                    .logout()
                        .logoutSuccessHandler(this.getLogoutSuccessHandler())
                .and()
                    .exceptionHandling()
                        .authenticationEntryPoint(this.getAuthenticationEntryPoint())
        ;

        /**
         * 세션 클러스트링
         */
//        http.sessionManagement()
//            .maximumSessions(1)
//            .maxSessionsPreventsLogin(true)
//            .expiredUrl("/")
//            .sessionRegistry(sessionRegistry());
    }


    /**
     * 세션 클러스트링
     * @return
     */
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    /**
     * 세션 클러스트링
     * @return
     */
    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
    }


    private AuthenticationEntryPoint getAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            ResultInfo resultInfo = ResultInfo.builder()
                    .code(SystemStatusCode.ACCESS_DENIED.getCode())
                    .message(authException.getMessage())
                    .build();

            if(!request.getMethod().equalsIgnoreCase("POST")) {
                response.sendRedirect("/");
                return;
            }

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().append(objectMapper.writeValueAsString(resultInfo));
        };
    }


    private AuthenticationSuccessHandler getAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setCode(SystemStatusCode.LOGIN_SUCCESS.getCode());

            Cookie cookie = new Cookie("session", request.getSession().getId());
            cookie.setPath("/");

            response.addCookie(cookie);
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().append(objectMapper.writeValueAsString(resultInfo));
        };
    }

    private AuthenticationFailureHandler geAuthenticationFailureHandler() {
        return (request, response, exception) -> {
            ResultInfo resultInfo = new ResultInfo();

            if (exception instanceof AdminException) {
                AdminException e = (AdminException) exception;
                resultInfo.setCode(e.getCode().getCode());
                resultInfo.setMessage(e.getMessage());
            } else if (exception instanceof BadCredentialsException) {
                resultInfo.setCode(SystemStatusCode.FAIL_LOGIN.getCode());
                resultInfo.setMessage(exception.getMessage());
            } else if (exception instanceof SessionAuthenticationException) {
                resultInfo.setCode(SystemStatusCode.FAIL_LOGIN.getCode());
                resultInfo.setMessage("이미 로그인된 사용자가 있습니다.");
            } else{
                log.error(exception.getMessage(), exception);
            }

            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().append(objectMapper.writeValueAsString(resultInfo));
        };
    }

    private LogoutSuccessHandler getLogoutSuccessHandler() {
        return (request, response, authentication) -> {
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setMessage(SystemStatusCode.LOGIN_SUCCESS.getCode());
            response.getWriter().append(objectMapper.writeValueAsString(resultInfo));
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
            try {
                LoginInfo loginInfo = ((AdminUser) userDetails).getLoginInfo();

                String presentedPassword = authentication.getCredentials().toString();

                if (!StringUtils.equals(CommonUtil.convertEncryptPassword(presentedPassword), userDetails.getPassword())) {
                    loginService.failLogin(loginInfo);
                }

                loginService.successLogin(loginInfo);

            } catch (AdminException e) {
                throw e;
            } catch (Exception e) {
                throw new AdminException(SystemStatusCode.INTERNAL_ERROR, e.getMessage());
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

            LoginInfo loginInfo = loginInfo2.get(0);

            List<GrantedAuthority> authorityList = new ArrayList<>();
            authorityList.add(new SimpleGrantedAuthority(loginInfo.getRoleCd()));

            return new AdminUser(loginInfo.getMbrId(), loginInfo.getMbrPw(), authorityList, loginInfo);
        }
    }
}
