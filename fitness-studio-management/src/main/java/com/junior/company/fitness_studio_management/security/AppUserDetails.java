package com.junior.company.fitness_studio_management.security;

import com.junior.company.fitness_studio_management.model.AppUser;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@NoArgsConstructor
public class AppUserDetails implements UserDetails {

    private String username;
    private String password;
    private boolean enabled;
    private Collection<? extends GrantedAuthority> authorities;
    private final static String ROLE_PREFIX = "ROLE_";

    public AppUserDetails(AppUser appUser) {
        this.username = appUser.getUsername();
        this.password = appUser.getPassword();
        this.enabled = appUser.isEnabled();
        this.authorities = appUser.getRoles().stream()
                .map((role) -> new SimpleGrantedAuthority(ROLE_PREFIX + role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
