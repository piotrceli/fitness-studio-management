package com.junior.company.fitness_studio_management.config;

import com.junior.company.fitness_studio_management.model.AppUser;
import com.junior.company.fitness_studio_management.model.DifficultyLevel;
import com.junior.company.fitness_studio_management.model.FitnessClass;
import com.junior.company.fitness_studio_management.model.GymEvent;
import com.junior.company.fitness_studio_management.model.Role;
import com.junior.company.fitness_studio_management.model.Trainer;
import com.junior.company.fitness_studio_management.repository.AppUserRepository;
import com.junior.company.fitness_studio_management.repository.FitnessClassRepository;
import com.junior.company.fitness_studio_management.repository.GymEventRepository;
import com.junior.company.fitness_studio_management.repository.RoleRepository;
import com.junior.company.fitness_studio_management.repository.TrainerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class SampleDataConfiguration {

    @Bean
    public CommandLineRunner commandLineRunner(AppUserRepository appUserRepository,
                                               FitnessClassRepository fitnessClassRepository,
                                               GymEventRepository gymEventRepository,
                                               RoleRepository roleRepository,
                                               TrainerRepository trainerRepository,
                                               PasswordEncoder passwordEncoder) {
        return args -> {

            // Adding sample roles

            Role adminRole = new Role(1L, "ADMIN");
            Role userRole = new Role(2L, "USER");
            roleRepository.saveAll(List.of(adminRole, userRole));

            // Adding sample users

            AppUser admin = AppUser.builder()
                    .id(1L)
                    .username("admin")
                    .password(passwordEncoder.encode("password"))
                    .roles(List.of(adminRole))
                    .firstName("admin")
                    .lastName("admin")
                    .email("admin@email.com")
                    .dob(LocalDate.of(2000, 1, 1))
                    .isEnabled(true)
                    .build();

            AppUser user = AppUser.builder()
                    .id(2L)
                    .username("user")
                    .password(passwordEncoder.encode("password"))
                    .roles(List.of(userRole))
                    .firstName("user")
                    .lastName("user")
                    .email("user@email.com")
                    .dob(LocalDate.of(2000, 1, 1))
                    .isEnabled(true)
                    .build();

            AppUser userOne = AppUser.builder()
                    .id(3L)
                    .username("mia")
                    .password(passwordEncoder.encode("password"))
                    .roles(List.of(userRole))
                    .firstName("mia")
                    .lastName("mia")
                    .email("mia@email.com")
                    .dob(LocalDate.of(2000, 1, 1))
                    .isEnabled(true)
                    .build();

            AppUser userTwo = AppUser.builder()
                    .id(4L)
                    .username("amelia")
                    .password(passwordEncoder.encode("password"))
                    .roles(List.of(userRole))
                    .firstName("amelia")
                    .lastName("amelia")
                    .email("amelia@email.com")
                    .dob(LocalDate.of(2000, 1, 1))
                    .isEnabled(true)
                    .build();

            AppUser userThree = AppUser.builder()
                    .id(5L)
                    .username("tom")
                    .password(passwordEncoder.encode("password"))
                    .roles(List.of(userRole))
                    .firstName("tom")
                    .lastName("tom")
                    .email("tom@email.com")
                    .dob(LocalDate.of(2000, 1, 1))
                    .isEnabled(true)
                    .build();

            AppUser userFour = AppUser.builder()
                    .id(6L)
                    .username("aron")
                    .password(passwordEncoder.encode("password"))
                    .roles(List.of(userRole))
                    .firstName("aron")
                    .lastName("aron")
                    .email("aron@email.com")
                    .dob(LocalDate.of(2000, 1, 1))
                    .isEnabled(true)
                    .build();

            appUserRepository.saveAll(List.of(admin, user, userOne, userTwo, userThree, userFour));

            // Adding sample trainers

            Trainer trainerOne = Trainer.builder()
                    .id(1L)
                    .firstName("Tim")
                    .lastName("Smith")
                    .email("tim@email.com")
                    .description("description_one")
                    .build();

            Trainer trainerTwo = Trainer.builder()
                    .id(2L)
                    .firstName("Sam")
                    .lastName("Johnson")
                    .email("sam@email.com")
                    .description("description_two")
                    .build();

            Trainer trainerThree = Trainer.builder()
                    .id(3L)
                    .firstName("Olivia")
                    .lastName("Miller")
                    .email("olivia@email.com")
                    .description("description_three")
                    .build();

            Trainer trainerFour = Trainer.builder()
                    .id(4L)
                    .firstName("Emma")
                    .lastName("Brown")
                    .email("emma@email.com")
                    .description("description_four")
                    .build();

            Trainer trainerFive = Trainer.builder()
                    .id(5L)
                    .firstName("Isabella")
                    .lastName("Williams")
                    .email("isabella@email.com")
                    .description("description_five")
                    .build();

            trainerRepository.saveAll(List.of(trainerOne, trainerTwo, trainerThree, trainerFour, trainerFive));

            // Adding sample fitness classes with trainers

            FitnessClass fitnessClassOne = FitnessClass.builder()
                    .id(1L)
                    .name("Yoga")
                    .difficultyLevel(DifficultyLevel.BEGINNER)
                    .description("description_one")
                    .trainers(List.of(trainerOne))
                    .build();

            FitnessClass fitnessClassTwo = FitnessClass.builder()
                    .id(2L)
                    .name("Boxing")
                    .difficultyLevel(DifficultyLevel.INTERMEDIATE)
                    .description("description_two")
                    .trainers(List.of(trainerTwo))
                    .build();

            FitnessClass fitnessClassThree = FitnessClass.builder()
                    .id(3L)
                    .name("Cycling")
                    .difficultyLevel(DifficultyLevel.ADVANCED)
                    .description("description_three")
                    .trainers(List.of(trainerThree))
                    .build();

            FitnessClass fitnessClassFour = FitnessClass.builder()
                    .id(4L)
                    .name("Stretching")
                    .difficultyLevel(DifficultyLevel.INTERMEDIATE)
                    .description("description_four")
                    .trainers(null)
                    .build();

            fitnessClassRepository.saveAll(List.of(
                    fitnessClassOne, fitnessClassTwo, fitnessClassThree, fitnessClassFour));

            // Adding sample gym events

            GymEvent gymEventOne = GymEvent.builder()
                    .id(1L)
                    .startTime(LocalDateTime.of(2023, 1, 1, 12, 0, 0))
                    .endTime(LocalDateTime.of(2023, 1, 1, 13, 0, 0))
                    .duration("01:00")
                    .participantsLimit(5)
                    .currentParticipantsNumber(2)
                    .enrolledParticipants(List.of(userOne, userTwo))
                    .fitnessClass(fitnessClassOne)
                    .build();

            GymEvent gymEventTwo = GymEvent.builder()
                    .id(2L)
                    .startTime(LocalDateTime.of(2023, 1, 1, 16, 0, 0))
                    .endTime(LocalDateTime.of(2023, 1, 1, 18, 30, 0))
                    .duration("01:30")
                    .participantsLimit(10)
                    .currentParticipantsNumber(3)
                    .enrolledParticipants(List.of(userOne, userTwo, userThree))
                    .fitnessClass(fitnessClassTwo)
                    .build();

            GymEvent gymEventThree = GymEvent.builder()
                    .id(3L)
                    .startTime(LocalDateTime.of(2023, 1, 1, 19, 0, 0))
                    .endTime(LocalDateTime.of(2023, 1, 1, 21, 0, 0))
                    .duration("02:00")
                    .participantsLimit(5)
                    .currentParticipantsNumber(5)
                    .enrolledParticipants(List.of(userOne, userTwo, userThree, userFour, user))
                    .fitnessClass(fitnessClassThree)
                    .build();

            GymEvent gymEventFour = GymEvent.builder()
                    .id(4L)
                    .startTime(LocalDateTime.of(2023, 1, 1, 17, 0, 0))
                    .endTime(LocalDateTime.of(2023, 1, 1, 18, 0, 0))
                    .duration("01:00")
                    .participantsLimit(8)
                    .currentParticipantsNumber(4)
                    .enrolledParticipants(List.of(userOne, userTwo, userThree, userFour))
                    .fitnessClass(fitnessClassFour)
                    .build();

            gymEventRepository.saveAll(List.of(gymEventOne, gymEventTwo, gymEventThree, gymEventFour));
        };
    }
}
