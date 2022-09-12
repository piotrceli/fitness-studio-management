package com.junior.company.fitness_studio_management.mapper;

import com.junior.company.fitness_studio_management.dto.AppUserRequest;
import com.junior.company.fitness_studio_management.dto.AppUserResponse;
import com.junior.company.fitness_studio_management.model.AppUser;

import java.util.List;
import java.util.stream.Collectors;

public class AppUserMapper {

    private static final Long EMPTY_ID = null;

    public static AppUser mapAppUserRequestToAppUserCreate(AppUserRequest appUserRequest) {

        return AppUser.builder()
                .id(EMPTY_ID)
                .username(appUserRequest.getUsername())
                .firstName(appUserRequest.getFirstName())
                .lastName(appUserRequest.getLastName())
                .email(appUserRequest.getEmail())
                .dob(appUserRequest.getDob())
                .build();
    }

    public static AppUser mapAppUserRequestToAppUserUpdate(AppUserRequest appUserRequest) {

        return AppUser.builder()
                .id(appUserRequest.getId())
                .firstName(appUserRequest.getFirstName())
                .lastName(appUserRequest.getLastName())
                .email(appUserRequest.getEmail())
                .dob(appUserRequest.getDob())
                .build();
    }

    public static AppUserResponse mapAppUserToAppUserResponse(AppUser appUser) {

        return AppUserResponse.builder()
                .username(appUser.getUsername())
                .firstName(appUser.getFirstName())
                .lastName(appUser.getLastName())
                .email(appUser.getEmail())
                .dob(appUser.getDob())
                .gymEvents(GymEventMapper.mapGymEventListToGymEventResponseList(appUser.getGymEvents()))
                .build();
    }

    public static List<AppUserResponse> mapAppUserListToAppUserResponseList(List<AppUser> appUsers) {

        return appUsers.stream()
                .map((appUser) ->
                        AppUserResponse.builder()
                                .id(appUser.getId())
                                .username(appUser.getUsername())
                                .roles(appUser.getRoles())
                                .firstName(appUser.getFirstName())
                                .lastName(appUser.getLastName())
                                .email(appUser.getEmail())
                                .dob(appUser.getDob())
                                .isEnabled(appUser.isEnabled())
                                .build())
                .collect(Collectors.toList());
    }

}
