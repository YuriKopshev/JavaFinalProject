package ru.netology.javadiplom.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.netology.javadiplom.model.User;

import java.util.Collection;

public class AccountUserDetails implements UserDetails {

    private String login;

    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public static AccountUserDetails fromUserEntityToCustomUserDetails(User user) {
        AccountUserDetails userDetails = new AccountUserDetails();
        userDetails.login = user.getLogin();
        userDetails.password = user.getPassword();
        return userDetails;
    }
}
