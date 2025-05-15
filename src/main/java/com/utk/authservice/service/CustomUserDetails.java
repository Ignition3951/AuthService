package com.utk.authservice.service;

import com.utk.authservice.entities.UserInfo;
import com.utk.authservice.entities.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails extends UserInfo implements UserDetails {

    private String userName;

    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(UserInfo byUserInfo) {
        this.userName = byUserInfo.getUserName();
        this.password = byUserInfo.getPassword();
        List<GrantedAuthority> auth = new ArrayList<>();

        for (UserRole userRole : byUserInfo.getUserRoles()) {
            auth.add(new SimpleGrantedAuthority(userRole.getRoleName().toUpperCase()));
        }
        this.authorities = auth;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
