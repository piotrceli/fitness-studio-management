package com.junior.company.fitness_studio_management.repository;

import com.junior.company.fitness_studio_management.model.AppUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository appUserRepository;

    private static String username;
    private static String email;
    private static AppUser appUser;

    @BeforeAll
    static void beforeAll() {
        username = "username";
        email = "email@email.com";
        appUser = AppUser.builder()
                .id(1L)
                .username(username)
                .password("password_one")
                .roles(null)
                .firstName("firstname_one")
                .lastName("lastname_one")
                .email(email)
                .dob(LocalDate.of(2000, 1, 1))
                .isEnabled(true)
                .build();
    }

    @Test
    void shouldFindAppUserByUsername_givenValidUsername() {

        // given
        appUserRepository.save(appUser);

        // when
        Optional<AppUser> result = appUserRepository.findByUsername(username);

        // then
        assertThat(result).isPresent();
    }

    @Test
    void shouldNotFindAppUserByUsername_givenInvalidUsername() {

        // given
        String username = "invalid";

        // when
        Optional<AppUser> result = appUserRepository.findByUsername(username);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldFindAppUserByEmail_givenValidEmail() {

        // given
        appUserRepository.save(appUser);

        // when
        Optional<AppUser> result = appUserRepository.findByEmail(email);

        // then
        assertThat(result).isPresent();
    }

    @Test
    void shouldNotFindAppUserByEmail_givenInvalidEmail() {

        // given
        String email = "invalid";

        // when
        Optional<AppUser> result = appUserRepository.findByEmail(email);

        // then
        assertThat(result).isEmpty();
    }
}