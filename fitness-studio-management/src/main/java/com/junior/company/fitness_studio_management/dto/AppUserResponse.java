package com.junior.company.fitness_studio_management.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.junior.company.fitness_studio_management.model.Role;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Getter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class AppUserResponse {

    private Long id;
    private String username;
    private List<Role> roles;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dob;
    private boolean isEnabled;
    private List<GymEventResponse> gymEvents;

}
