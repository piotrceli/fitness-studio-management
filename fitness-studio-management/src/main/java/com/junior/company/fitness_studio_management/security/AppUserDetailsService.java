package com.junior.company.fitness_studio_management.security;

import com.junior.company.fitness_studio_management.model.AppUser;
import com.junior.company.fitness_studio_management.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username: {}", username);
        AppUser appUser = appUserRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format("Username %s not found", username)));

        return new AppUserDetails(appUser);
    }

}
