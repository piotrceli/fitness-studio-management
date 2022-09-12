package com.junior.company.fitness_studio_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junior.company.fitness_studio_management.dto.AppUserRequest;
import com.junior.company.fitness_studio_management.dto.AppUserResponse;
import com.junior.company.fitness_studio_management.exception.PermissionDeniedException;
import com.junior.company.fitness_studio_management.exception.ResourceNotFoundException;
import com.junior.company.fitness_studio_management.mapper.AppUserMapper;
import com.junior.company.fitness_studio_management.model.AppUser;
import com.junior.company.fitness_studio_management.model.Response;
import com.junior.company.fitness_studio_management.security.AppUserDetailsService;
import com.junior.company.fitness_studio_management.service.AppUserService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppUserController.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
class AppUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AppUserService appUserService;

    @MockBean
    private AppUserDetailsService appUserDetailsService;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldGetListOfUsers() throws Exception {

        // given
        AppUser appUserOne = AppUser.builder()
                .id(1L)
                .username("username_one")
                .password("password_one")
                .roles(null)
                .firstName("firstname_one")
                .lastName("lastname_one")
                .email("one@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .isEnabled(true)
                .build();

        AppUser appUserTwo = AppUser.builder()
                .id(1L)
                .username("username_two")
                .password("password_two")
                .roles(null)
                .firstName("firstname_two")
                .lastName("lastname_two")
                .email("two@email.com")
                .dob(LocalDate.of(2002, 2, 2))
                .isEnabled(true)
                .build();

        List<AppUser> appUsers = List.of(appUserOne, appUserTwo);

        given(appUserService.findAllUsers()).willReturn(
                AppUserMapper.mapAppUserListToAppUserResponseList(appUsers));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Retrieved list of users")
                .data(Map.of("users", appUserService.findAllUsers()))
                .build();

        // when then
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void shouldGetUserById_givenValidUserId() throws Exception {

        // given
        Long userId = 1L;
        AppUser appUser = AppUser.builder()
                .id(userId)
                .username("username_one")
                .password("password_one")
                .roles(null)
                .firstName("firstname_one")
                .lastName("lastname_one")
                .email("one@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .isEnabled(true)
                .gymEvents(new ArrayList<>())
                .build();

        AppUserResponse appUserResponse = AppUserMapper.mapAppUserToAppUserResponse(appUser);

        given(appUserService.findUserById(userId)).willReturn(appUserResponse);

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(String.format("Retrieved user by id: %s", userId))
                .data(Map.of("user", appUserService.findUserById(userId)))
                .build();

        // when then
        mockMvc.perform(get("/api/v1/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void shouldNotGetUserById_whenUserGotNoPermission() throws Exception {

        // given
        Long userId = 1L;

        given(appUserService.findUserById(userId)).willThrow(
                new PermissionDeniedException("Permission denied"));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Permission denied")
                .build();

        // when then
        mockMvc.perform(get("/api/v1/users/{userId}", userId))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void shouldNotGetUserById_givenInvalidUserId() throws Exception {

        // given
        Long userId = 0L;

        given(appUserService.findUserById(userId)).willThrow(
                new ResourceNotFoundException(String.format("User with id: %s not found", userId)));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.NOT_FOUND)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(String.format("User with id: %s not found", userId))
                .build();

        // when then
        mockMvc.perform(get("/api/v1/users/{userId}", userId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    void shouldRegisterUser_givenValidAppUserRequest() throws Exception {

        // given
        AppUserRequest appUserRequest = AppUserRequest.builder()
                .username("username")
                .password("password")
                .matchingPassword("password")
                .firstName("firstname")
                .lastName("lastname")
                .email("email@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .build();

        given(appUserService.registerUser(any())).willReturn(true);

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .message("Registered new user")
                .data(Map.of("is_registered", appUserService.registerUser(appUserRequest)))
                .build();

        // when then
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appUserRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    void shouldNotRegisterUser_givenInvalidAppUserRequestUsername() throws Exception {

        // given
        AppUserRequest appUserRequest = AppUserRequest.builder()
                .username("u")
                .password("password")
                .matchingPassword("password")
                .firstName("firstname")
                .lastName("lastname")
                .email("email@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .build();

        Map<String, String> errors = new HashMap<>();
        String fieldName = "username";
        String errorMessage = "Min length is 2";
        errors.put(fieldName, errorMessage);
        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("error occurred")
                .data(Map.of("errors", errors))
                .build();

        // when then
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appUserRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    void shouldNotRegisterUser_givenAlreadyTakenUsername() throws Exception {

        // given
        AppUserRequest appUserRequest = AppUserRequest.builder()
                .username("username")
                .password("password")
                .matchingPassword("password")
                .firstName("firstname")
                .lastName("lastname")
                .email("email@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .build();

        given(appUserService.registerUser(any())).willThrow(
                new IllegalStateException(String.format("Username %s is already taken", appUserRequest.getUsername())));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(String.format("Username %s is already taken", appUserRequest.getUsername()))
                .build();

        // when then
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appUserRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    void shouldNotRegisterUser_givenAlreadyTakenEmail() throws Exception {

        // given
        AppUserRequest appUserRequest = AppUserRequest.builder()
                .username("username")
                .password("password")
                .matchingPassword("password")
                .firstName("firstname")
                .lastName("lastname")
                .email("email@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .build();

        given(appUserService.registerUser(any())).willThrow(
                new IllegalStateException(String.format("Email %s is already taken", appUserRequest.getEmail())));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(String.format("Email %s is already taken", appUserRequest.getEmail()))
                .build();

        // when then
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appUserRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void shouldUpdateUser_givenValidAppUserRequest() throws Exception {

        // given
        AppUserRequest appUserRequest = AppUserRequest.builder()
                .id(1L)
                .username("username")
                .password("password")
                .matchingPassword("password")
                .firstName("firstname")
                .lastName("lastname")
                .email("email@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .build();

        given(appUserService.updateUser(any())).willReturn(true);

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Updated user")
                .data(Map.of("is_updated", appUserService.updateUser(appUserRequest)))
                .build();

        // when then
        mockMvc.perform(put("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appUserRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void shouldNotUpdateUser_givenUserGotNoPermission() throws Exception {

        // given
        AppUserRequest appUserRequest = AppUserRequest.builder()
                .id(1L)
                .username("username")
                .password("password")
                .matchingPassword("password")
                .firstName("firstname")
                .lastName("lastname")
                .email("email@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .build();

        given(appUserService.updateUser(any())).willThrow(
                new PermissionDeniedException("Permission denied"));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Permission denied")
                .build();

        // when then
        mockMvc.perform(put("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appUserRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void shouldNotUpdateUser_givenAlreadyTakenEmail() throws Exception {

        // given
        AppUserRequest appUserRequest = AppUserRequest.builder()
                .id(1L)
                .username("username")
                .password("password")
                .matchingPassword("password")
                .firstName("firstname")
                .lastName("lastname")
                .email("email@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .build();

        given(appUserService.updateUser(any())).willThrow(
                new IllegalStateException(String.format("Email %s is already taken", appUserRequest.getEmail())));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(String.format("Email %s is already taken", appUserRequest.getEmail()))
                .build();

        // when then
        mockMvc.perform(put("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appUserRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void shouldDeleteUserById_givenValidUserId() throws Exception{

        // given
        Long userId = 1L;

        given(appUserService.deleteUserById(userId)).willReturn(true);

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(String.format("Deleted user with id: %s", userId))
                .data(Map.of("is_deleted", appUserService.deleteUserById(userId)))
                .build();

        // when then
        mockMvc.perform(delete("/api/v1/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void shouldNotDeleteUserById_givenUserGotNoPermission() throws Exception{

        // given
        Long userId = 1L;

        given(appUserService.deleteUserById(userId)).willThrow(
                new PermissionDeniedException("Permission denied"));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Permission denied")
                .build();

        // when then
        mockMvc.perform(delete("/api/v1/users/{userId}", userId))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void shouldNotDeleteUserById_givenInvalidUserId() throws Exception{

        // given
        Long userId = 0L;

        given(appUserService.deleteUserById(userId)).willThrow(
                new ResourceNotFoundException(String.format("User with id: %s not found", userId)));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.NOT_FOUND)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(String.format("User with id: %s not found", userId))
                .build();

        // when then
        mockMvc.perform(delete("/api/v1/users/{userId}", userId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }
}