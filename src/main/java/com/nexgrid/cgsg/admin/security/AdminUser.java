package com.nexgrid.cgsg.admin.security;

import com.nexgrid.cgsg.admin.vo.LoginInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminUser extends User {

    private LoginInfo loginInfo;

    public AdminUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public AdminUser(String username, String password, Collection<? extends GrantedAuthority> authorities, LoginInfo loginInfo) {
        super(username, password, authorities);
        this.loginInfo = loginInfo;
    }
}
