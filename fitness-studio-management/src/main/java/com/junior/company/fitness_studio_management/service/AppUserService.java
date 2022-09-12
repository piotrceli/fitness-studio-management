package com.junior.company.fitness_studio_management.service;

import com.junior.company.fitness_studio_management.dto.AppUserRequest;
import com.junior.company.fitness_studio_management.dto.AppUserResponse;
import com.junior.company.fitness_studio_management.model.AppUser;

import java.util.List;

public interface AppUserService {

    List<AppUserResponse> findAllUsers();

    AppUserResponse findUserById(Long userId);

    AppUser getCurrentUser();

    boolean registerUser(AppUserRequest appUserRequest);

    boolean updateUser(AppUserRequest appUserRequest);

    boolean deleteUserById(Long userId);
}
