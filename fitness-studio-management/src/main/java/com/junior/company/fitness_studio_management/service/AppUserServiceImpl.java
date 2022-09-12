package com.junior.company.fitness_studio_management.service;

import com.junior.company.fitness_studio_management.dto.AppUserRequest;
import com.junior.company.fitness_studio_management.dto.AppUserResponse;
import com.junior.company.fitness_studio_management.exception.PermissionDeniedException;
import com.junior.company.fitness_studio_management.exception.ResourceNotFoundException;
import com.junior.company.fitness_studio_management.mapper.AppUserMapper;
import com.junior.company.fitness_studio_management.model.AppUser;
import com.junior.company.fitness_studio_management.model.Role;
import com.junior.company.fitness_studio_management.repository.AppUserRepository;
import com.junior.company.fitness_studio_management.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<AppUserResponse> findAllUsers() {
        log.info("Retrieving list of users");
        List<AppUser> appUsers = appUserRepository.findAll();
        return AppUserMapper.mapAppUserListToAppUserResponseList(appUsers);
    }

    @Override
    public AppUserResponse findUserById(Long userId) {
        log.info("Retrieving user with id: {}", userId);
        AppUser appUser = appUserRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("User with id: %s not found", userId)));
        for (Role role : getCurrentUser().getRoles()) {
            if (Objects.equals(role.getName(), "ADMIN") ||
                    Objects.equals(getCurrentUser().getId(), userId)) {
                return AppUserMapper.mapAppUserToAppUserResponse(appUser);
            }
        }
        throw new PermissionDeniedException("Permission denied");
    }

    @Override
    public AppUser getCurrentUser() {
        Principal principal = SecurityContextHolder
                .getContext()
                .getAuthentication();
        return appUserRepository.findByUsername(principal.getName()).orElseThrow(() ->
                new ResourceNotFoundException(String.format("User with username %s not found", principal.getName())));
    }

    @Override
    public boolean registerUser(AppUserRequest appUserRequest) {
        log.info("Registering user with username: {}", appUserRequest.getUsername());
        if (appUserRepository.findByUsername(appUserRequest.getUsername()).isPresent()) {
            throw new IllegalStateException(String.format("Username %s is already taken", appUserRequest.getUsername()));
        }
        if (appUserRepository.findByEmail(appUserRequest.getEmail()).isPresent()) {
            throw new IllegalStateException(String.format("Email %s is already taken", appUserRequest.getEmail()));
        }

        AppUser appUser = AppUserMapper.mapAppUserRequestToAppUserCreate(appUserRequest);
        appUser.setEnabled(true);
        appUser.setPassword(passwordEncoder.encode(appUserRequest.getPassword()));

        Optional<Role> optionalRole = roleRepository.findByName("USER");
        List<Role> roles = new ArrayList<>();
        optionalRole.ifPresent(roles::add);
        appUser.setRoles(roles);
        appUserRepository.save(appUser);
        return true;
    }

    @Override
    public boolean updateUser(AppUserRequest appUserRequest) {
        log.info("Registering user with username: {}", appUserRequest.getUsername());
        if (!Objects.equals(appUserRequest.getId(), getCurrentUser().getId())) {
            throw new PermissionDeniedException("Permission denied");
        }
        Optional<AppUser> optionalAppUser = appUserRepository.findByEmail(appUserRequest.getEmail());
        if (optionalAppUser.isPresent() &&
                !Objects.equals(appUserRequest.getId(), optionalAppUser.get().getId())) {
            throw new IllegalStateException(
                    String.format("Email %s is already taken", appUserRequest.getEmail()));
        }
        AppUser updatedAppUser = AppUserMapper.mapAppUserRequestToAppUserUpdate(appUserRequest);
        updatedAppUser.setUsername(getCurrentUser().getUsername());
        updatedAppUser.setEnabled(getCurrentUser().isEnabled());
        updatedAppUser.setPassword(passwordEncoder.encode(appUserRequest.getPassword()));
        updatedAppUser.setRoles(getCurrentUser().getRoles());
        appUserRepository.save(updatedAppUser);
        return true;
    }

    @Override
    public boolean deleteUserById(Long userId) {
        log.info("Deleting user with id: {}", userId);
        AppUser appUser = appUserRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("User with id: %s not found", userId)));
        for (Role role : getCurrentUser().getRoles()) {
            if (Objects.equals(role.getName(), "ADMIN") ||
                    (Objects.equals(getCurrentUser().getId(), userId))) {
                appUserRepository.delete(appUser);
                return true;
            }
        }
        throw new PermissionDeniedException("Permission denied");
    }
}
