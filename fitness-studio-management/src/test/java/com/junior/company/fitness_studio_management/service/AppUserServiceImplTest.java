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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
class AppUserServiceImplTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private AppUserServiceImpl appUserService;

    @Test
    void shouldGetListOfUsers() {

        // when
        appUserService.findAllUsers();

        // then
        verify(appUserRepository, times(1)).findAll();
    }

    @Test
    void shouldFindUserById_whenCurrentUserIsAdmin_givenValidUserId() {

        // given
        Long userId = 1L;
        Role adminRole = new Role(1L, "ADMIN");
        AppUser appUser = AppUser.builder()
                .id(userId)
                .username("admin")
                .password("password_one")
                .roles(List.of(adminRole))
                .firstName("firstname_one")
                .lastName("lastname_one")
                .email("one@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .isEnabled(true)
                .gymEvents(new ArrayList<>())
                .build();

        given(appUserRepository.findById(userId)).willReturn(Optional.of(appUser));

        SecurityContextHolder.setContext(securityContext);
        given(securityContext.getAuthentication()).willReturn(authentication);
        given(authentication.getName()).willReturn("admin");

        given(appUserRepository.findByUsername("admin")).willReturn(Optional.of(appUser));


        AppUserResponse appUserResponse = AppUserMapper.mapAppUserToAppUserResponse(appUser);

        // when
        AppUserResponse result = appUserService.findUserById(userId);

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(appUserResponse);
    }

    @Test
    void shouldFindUserById_whenCurrentUserIdIsEqualsUserId_givenValidUserId() {

        // given
        Long userId = 1L;
        Role userRole = new Role(1L, "USER");
        AppUser appUser = AppUser.builder()
                .id(userId)
                .username("user")
                .password("password_one")
                .roles(List.of(userRole))
                .firstName("firstname_one")
                .lastName("lastname_one")
                .email("one@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .isEnabled(true)
                .gymEvents(new ArrayList<>())
                .build();

        given(appUserRepository.findById(userId)).willReturn(Optional.of(appUser));

        SecurityContextHolder.setContext(securityContext);
        given(securityContext.getAuthentication()).willReturn(authentication);
        given(authentication.getName()).willReturn("user");

        given(appUserRepository.findByUsername("user")).willReturn(Optional.of(appUser));

        AppUserResponse appUserResponse = AppUserMapper.mapAppUserToAppUserResponse(appUser);

        // when
        AppUserResponse result = appUserService.findUserById(userId);

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(appUserResponse);
    }

    @Test
    void shouldNotFindUserById_givenInvalidUserId() {

        // given
        Long userId = 0L;

        given(appUserRepository.findById(userId)).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> appUserService.findUserById(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("User with id: %s not found", userId));
    }

    @Test
    void shouldNotFindUserById_whenCurrentUserIdIsNotEqualsUserId_givenValidUserId() {

        // given
        Long userId = 1L;
        Role userRole = new Role(1L, "USER");
        AppUser appUser = AppUser.builder()
                .id(userId)
                .username("user")
                .password("password_one")
                .roles(List.of(userRole))
                .firstName("firstname_one")
                .lastName("lastname_one")
                .email("one@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .isEnabled(true)
                .build();

        AppUser currentAppUser = AppUser.builder()
                .id(2L)
                .username("current_user")
                .password("password_one")
                .roles(List.of(userRole))
                .firstName("firstname_one")
                .lastName("lastname_one")
                .email("one@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .isEnabled(true)
                .build();

        given(appUserRepository.findById(userId)).willReturn(Optional.of(appUser));

        SecurityContextHolder.setContext(securityContext);
        given(securityContext.getAuthentication()).willReturn(authentication);
        given(authentication.getName()).willReturn("current_user");

        given(appUserRepository.findByUsername("current_user")).willReturn(Optional.of(currentAppUser));

        // when then
        assertThatThrownBy(() -> appUserService.findUserById(userId))
                .isInstanceOf(PermissionDeniedException.class)
                .hasMessageContaining("Permission denied");
    }

    @Test
    void shouldGetCurrentUser() {

        // given
        AppUser appUser = AppUser.builder()
                .id(1L)
                .username("user")
                .password("password_one")
                .roles(null)
                .firstName("firstname_one")
                .lastName("lastname_one")
                .email("one@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .isEnabled(true)
                .build();

        SecurityContextHolder.setContext(securityContext);
        given(securityContext.getAuthentication()).willReturn(authentication);
        given(authentication.getName()).willReturn(appUser.getUsername());

        given(appUserRepository.findByUsername("user")).willReturn(Optional.of(appUser));

        // when
        AppUser result = appUserService.getCurrentUser();

        // then
        assertThat(result).isEqualTo(appUser);
    }

    @Test
    void shouldNotGetCurrentUser() {

        // given
        String username = "current_user";

        SecurityContextHolder.setContext(securityContext);
        given(securityContext.getAuthentication()).willReturn(authentication);
        given(authentication.getName()).willReturn(username);

        given(appUserRepository.findByUsername(username)).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> appUserService.getCurrentUser())
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format(
                        "User with username %s not found", username));
    }

    @Test
    void shouldRegisterUser_givenValidAppUserRequest() {

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

        Role userRole = new Role(1L, "USER");

        given(appUserRepository.findByUsername(anyString())).willReturn(Optional.empty());
        given(appUserRepository.findByEmail(anyString())).willReturn(Optional.empty());
        given(roleRepository.findByName(anyString())).willReturn(Optional.of(userRole));

        AppUser appUser = AppUserMapper.mapAppUserRequestToAppUserCreate(appUserRequest);
        appUser.setEnabled(true);
        appUser.setPassword(passwordEncoder.encode(appUserRequest.getPassword()));

        appUser.setRoles(new ArrayList<>(List.of(userRole)));

        // when
        boolean result = appUserService.registerUser(appUserRequest);

        // then
        ArgumentCaptor<AppUser> appUserArgumentCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserRepository).save(appUserArgumentCaptor.capture());
        AppUser capturedAppUser = appUserArgumentCaptor.getValue();

        assertThat(capturedAppUser).usingRecursiveComparison().isEqualTo(appUser);
        assertThat(result).isTrue();
    }

    @Test
    void shouldNotRegisterUser_givenUsernameIsAlreadyTaken() {

        // given
        String username = "username";
        AppUser appUser = AppUser.builder()
                .id(1L)
                .username(username)
                .password("password_one")
                .roles(null)
                .firstName("firstname_one")
                .lastName("lastname_one")
                .email("one@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .isEnabled(true)
                .build();

        AppUserRequest appUserRequest = AppUserRequest.builder()
                .username(username)
                .password("password")
                .matchingPassword("password")
                .firstName("firstname")
                .lastName("lastname")
                .email("email@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .build();

        given(appUserRepository.findByUsername(username)).willReturn(Optional.of(appUser));

        // when then
        assertThatThrownBy(() -> appUserService.registerUser(appUserRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format(
                        "Username %s is already taken", appUserRequest.getUsername()));
    }

    @Test
    void shouldNotRegisterUser_givenEmailIsAlreadyTaken() {

        // given
        String email = "email@email.com";
        AppUser appUser = AppUser.builder()
                .id(1L)
                .username("username_one")
                .password("password_one")
                .roles(null)
                .firstName("firstname_one")
                .lastName("lastname_one")
                .email(email)
                .dob(LocalDate.of(2000, 1, 1))
                .isEnabled(true)
                .build();

        AppUserRequest appUserRequest = AppUserRequest.builder()
                .username("username_two")
                .password("password")
                .matchingPassword("password")
                .firstName("firstname")
                .lastName("lastname")
                .email(email)
                .dob(LocalDate.of(2000, 1, 1))
                .build();

        given(appUserRepository.findByUsername(anyString())).willReturn(Optional.empty());
        given(appUserRepository.findByEmail(email)).willReturn(Optional.of(appUser));

        // when then
        assertThatThrownBy(() -> appUserService.registerUser(appUserRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format(
                        "Email %s is already taken", appUserRequest.getEmail()));
    }

    @Test
    void shouldUpdateUser_givenValidAppUserRequest() {

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

        AppUser currentUser = AppUser.builder()
                .id(1L)
                .username("username")
                .password("password")
                .roles(null)
                .firstName("firstname")
                .lastName("lastname")
                .email("email@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .isEnabled(true)
                .build();

        SecurityContextHolder.setContext(securityContext);
        given(securityContext.getAuthentication()).willReturn(authentication);
        given(appUserRepository.findByUsername(authentication.getName())).willReturn(Optional.of(currentUser));

        given(appUserRepository.findByEmail(appUserRequest.getEmail())).willReturn(Optional.empty());

        AppUser updatedAppUser = AppUserMapper.mapAppUserRequestToAppUserUpdate(appUserRequest);
        updatedAppUser.setUsername(currentUser.getUsername());
        updatedAppUser.setEnabled(currentUser.isEnabled());
        updatedAppUser.setPassword(passwordEncoder.encode(appUserRequest.getPassword()));
        updatedAppUser.setRoles(currentUser.getRoles());

        given(appUserRepository.save(any())).willReturn(updatedAppUser);

        // when
        boolean result = appUserService.updateUser(appUserRequest);

        // then
        ArgumentCaptor<AppUser> appUserArgumentCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserRepository).save(appUserArgumentCaptor.capture());
        AppUser capturedAppUser = appUserArgumentCaptor.getValue();

        assertThat(capturedAppUser).usingRecursiveComparison().isEqualTo(updatedAppUser);
        assertThat(result).isTrue();
    }

    @Test
    void shouldNotUpdateUser_whenCurrentUserGotNoPermission() {

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

        AppUser currentUser = AppUser.builder()
                .id(22L)
                .username("username")
                .password("password")
                .roles(null)
                .firstName("firstname")
                .lastName("lastname")
                .email("email@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .isEnabled(true)
                .build();

        SecurityContextHolder.setContext(securityContext);
        given(securityContext.getAuthentication()).willReturn(authentication);
        given(appUserRepository.findByUsername(authentication.getName())).willReturn(Optional.of(currentUser));

        // when then
        assertThatThrownBy(() -> appUserService.updateUser(appUserRequest))
                .isInstanceOf(PermissionDeniedException.class)
                .hasMessageContaining("Permission denied");
    }

    @Test
    void shouldNotUpdateUser_givenAppUserRequestEmailIsAlreadyTaken() {

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

        AppUser currentUser = AppUser.builder()
                .id(1L)
                .username("username")
                .password("password")
                .roles(null)
                .firstName("firstname")
                .lastName("lastname")
                .email("email@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .isEnabled(true)
                .build();

        AppUser anotherUser = AppUser.builder()
                .id(22L)
                .username("username_one")
                .password("password_one")
                .roles(null)
                .firstName("firstname_one")
                .lastName("lastname_one")
                .email("email@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .isEnabled(true)
                .build();

        SecurityContextHolder.setContext(securityContext);
        given(securityContext.getAuthentication()).willReturn(authentication);
        given(appUserRepository.findByUsername(authentication.getName())).willReturn(Optional.of(currentUser));

        given(appUserRepository.findByEmail(appUserRequest.getEmail())).willReturn(Optional.of(anotherUser));

        // when then
        assertThatThrownBy(() -> appUserService.updateUser(appUserRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("Email %s is already taken", appUserRequest.getEmail()));
    }

    @Test
    void shouldDeleteUserById_whenCurrentUserIsAdmin_givenValidUserId() {

        // given
        Long userId = 1L;
        Role adminRole = new Role(1L, "ADMIN");
        AppUser currentUser = AppUser.builder()
                .id(userId)
                .username("username")
                .password("password")
                .roles(List.of(adminRole))
                .firstName("firstname")
                .lastName("lastname")
                .email("email@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .isEnabled(true)
                .build();

        given(appUserRepository.findById(userId)).willReturn(Optional.of(currentUser));

        SecurityContextHolder.setContext(securityContext);
        given(securityContext.getAuthentication()).willReturn(authentication);
        given(appUserRepository.findByUsername(authentication.getName())).willReturn(Optional.of(currentUser));

        // when
        boolean result = appUserService.deleteUserById(userId);

        // then
        assertThat(result).isTrue();
        verify(appUserRepository, times(1)).delete(currentUser);

    }

    @Test
    void shouldDeleteUserById_whenCurrentUserIsUser_givenValidUserId() {

        // given
        Long userId = 1L;
        Role userRole = new Role(1L, "USER");
        AppUser currentUser = AppUser.builder()
                .id(userId)
                .username("username")
                .password("password")
                .roles(List.of(userRole))
                .firstName("firstname")
                .lastName("lastname")
                .email("email@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .isEnabled(true)
                .build();

        given(appUserRepository.findById(userId)).willReturn(Optional.of(currentUser));

        SecurityContextHolder.setContext(securityContext);
        given(securityContext.getAuthentication()).willReturn(authentication);
        given(appUserRepository.findByUsername(authentication.getName())).willReturn(Optional.of(currentUser));

        // when
        boolean result = appUserService.deleteUserById(userId);

        // then
        assertThat(result).isTrue();
        verify(appUserRepository, times(1)).delete(currentUser);
    }

    @Test
    void shouldNotDeleteUserById_whenCurrentUserGotNoPermission() {

        // given
        Long userId = 1L;
        Role userRole = new Role(1L, "USER");
        AppUser currentUser = AppUser.builder()
                .id(2L)
                .username("username")
                .password("password")
                .roles(List.of(userRole))
                .firstName("firstname")
                .lastName("lastname")
                .email("email@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .isEnabled(true)
                .build();

        AppUser anotherUser = AppUser.builder()
                .id(userId)
                .username("username")
                .password("password")
                .roles(List.of(userRole))
                .firstName("firstname")
                .lastName("lastname")
                .email("email@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .isEnabled(true)
                .build();

        given(appUserRepository.findById(userId)).willReturn(Optional.of(anotherUser));

        SecurityContextHolder.setContext(securityContext);
        given(securityContext.getAuthentication()).willReturn(authentication);
        given(appUserRepository.findByUsername(authentication.getName())).willReturn(Optional.of(currentUser));

        // when then
        assertThatThrownBy(() -> appUserService.deleteUserById(userId))
                .isInstanceOf(PermissionDeniedException.class)
                .hasMessageContaining("Permission denied");
    }

    @Test
    void shouldNotDeleteUserById_givenInvalidUserId() {

        // given
        Long userId = 0L;

        given(appUserRepository.findById(userId)).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> appUserService.deleteUserById(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("User with id: %s not found", userId));
    }
}