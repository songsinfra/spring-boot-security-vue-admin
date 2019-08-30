package com.nexgrid.cgsg.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
//@EnableWebSecurity
public class AuthenticationConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private UserDetailsService customAuthenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(customAuthenticationProvider);
        auth.inMemoryAuthentication().withUser("test").password("{noop}test").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/test/**").access("hasRole('ROLE_USER')")
                .antMatchers("/**").permitAll()
                .and().formLogin();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //    @Service
//    public static class CustomAuthenticationProvider implements UserDetailsService {
//
//        @Autowired
//        private LoginService loginService;
//
//        @Override
//        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//            List<LoginInfo> loginInfo2 = loginService.getLoginInfo2(LoginInfo.builder().mbrId(username).build());
//
//            if (loginInfo2 == null && loginInfo2.size() == 0) throw new UsernameNotFoundException("Username Not Found");
//
//            List<GrantedAuthority> authorityList = new ArrayList<>();
//            authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
//
//            LoginInfo loginInfo = loginInfo2.get(0);
//            return new User(loginInfo.getMbrId(), loginInfo.getMbrPw(), authorityList);
//        }
//    }


}
