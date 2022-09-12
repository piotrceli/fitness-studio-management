package com.junior.company.fitness_studio_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junior.company.fitness_studio_management.dto.TrainerRequest;
import com.junior.company.fitness_studio_management.exception.ResourceNotFoundException;
import com.junior.company.fitness_studio_management.model.Response;
import com.junior.company.fitness_studio_management.model.Trainer;
import com.junior.company.fitness_studio_management.security.AppUserDetailsService;
import com.junior.company.fitness_studio_management.service.TrainerService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainerController.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
class TrainerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TrainerService trainerService;

    @MockBean
    private AppUserDetailsService appUserDetailsService;


    @Test
    void shouldGetListOfTrainers() throws Exception {

        // given
        Trainer trainerOne = Trainer.builder()
                .id(1L)
                .firstName("first_name")
                .lastName("last_name")
                .email("email@email.com")
                .description("description")
                .build();

        Trainer trainerTwo = Trainer.builder()
                .id(1L)
                .firstName("first_name")
                .lastName("last_name")
                .email("email@email.com")
                .description("description")
                .build();

        List<Trainer> trainers = List.of(trainerOne, trainerTwo);

        given(trainerService.findAllTrainers()).willReturn(trainers);

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Retrieved list of trainers")
                .data(Map.of("trainers", trainerService.findAllTrainers()))
                .build();

        // when then
        mockMvc.perform(get("/api/v1/trainers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    void shouldGetTrainerById_givenValidTrainerId() throws Exception {

        // given
        Long trainerId = 1L;
        Trainer trainer = Trainer.builder()
                .id(trainerId)
                .firstName("first_name")
                .lastName("last_name")
                .email("email@email.com")
                .description("description")
                .build();

        given(trainerService.findTrainerById(anyLong())).willReturn(trainer);

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(String.format("Retrieved trainer by id: %s", trainerId))
                .data(Map.of("trainer", trainerService.findTrainerById(trainerId)))
                .build();

        // when then
        mockMvc.perform(get("/api/v1/trainers/{trainerId}", trainerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    void shouldNotGetTrainerById_givenInvalidTrainerId() throws Exception {

        // given
        Long trainerId = 0L;

        given(trainerService.findTrainerById(anyLong())).willThrow(
                new ResourceNotFoundException(String.format("Trainer with id: %s not found", trainerId)));


        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.NOT_FOUND)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(String.format("Trainer with id: %s not found", trainerId))
                .build();

        // when then
        mockMvc.perform(get("/api/v1/trainers/{trainerId}", trainerId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldCreateTrainer_givenValidTrainerRequest() throws Exception {

        // given
        TrainerRequest trainerRequest = TrainerRequest.builder()
                .id(1L)
                .firstName("first_name")
                .lastName("last_name")
                .email("email@email.com")
                .description("description")
                .build();

        Trainer trainer = Trainer.builder()
                .id(1L)
                .firstName("first_name")
                .lastName("last_name")
                .email("email@email.com")
                .description("description")
                .build();

        given(trainerService.createTrainer(any())).willReturn(trainer);

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .message("Created new trainer")
                .data(Map.of("trainer", trainerService.createTrainer(trainerRequest)))
                .build();

        // when then
        mockMvc.perform(post("/api/v1/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldNotCreateTrainer_givenInvalidTrainerRequest() throws Exception {

        // given
        TrainerRequest trainerRequest = TrainerRequest.builder()
                .id(1L)
                .firstName("first_name")
                .lastName("last_name")
                .email("email")
                .description("description")
                .build();

        Map<String, String> errors = new HashMap<>();
        String fieldName = "email";
        String errorMessage = "Invalid email";
        errors.put(fieldName, errorMessage);
        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("error occurred")
                .data(Map.of("errors", errors))
                .build();

        // when then
        mockMvc.perform(post("/api/v1/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldUpdateTrainer_givenValidTrainerRequest() throws Exception {

        // given
        TrainerRequest trainerRequest = TrainerRequest.builder()
                .id(1L)
                .firstName("first_name")
                .lastName("last_name")
                .email("email@email.com")
                .description("description")
                .build();

        Trainer trainer = Trainer.builder()
                .id(1L)
                .firstName("first_name")
                .lastName("last_name")
                .email("email@email.com")
                .description("description")
                .build();

        given(trainerService.updateTrainer(any())).willReturn(trainer);

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Updated trainer")
                .data(Map.of("trainer", trainerService.updateTrainer(trainerRequest)))
                .build();

        // when then
        mockMvc.perform(put("/api/v1/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldNotUpdateTrainer_givenInvalidTrainerRequestId() throws Exception {

        // given
        Long trainerId = 0L;
        TrainerRequest trainerRequest = TrainerRequest.builder()
                .id(trainerId)
                .firstName("first_name")
                .lastName("last_name")
                .email("email@email.com")
                .description("description")
                .build();

        given(trainerService.updateTrainer(any())).willThrow(
                new ResourceNotFoundException(String.format("Trainer with id: %s not found", trainerRequest.getId())));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.NOT_FOUND)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(String.format("Trainer with id: %s not found", trainerRequest.getId()))
                .build();

        // when then
        mockMvc.perform(put("/api/v1/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldDeleteTrainerById_givenValidTrainerId() throws Exception{

        // given
        Long trainerId = 1L;

        given(trainerService.deleteTrainerById(anyLong())).willReturn(true);

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(String.format("Deleted trainer with id: %s", trainerId))
                .data(Map.of("is_deleted", trainerService.deleteTrainerById(trainerId)))
                .build();

        // when then
        mockMvc.perform(delete("/api/v1/trainers/{trainerId}", trainerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldNotDeleteFitnessClassById_givenInvalidFitnessClassId() throws Exception {

        // given
        Long trainerId = 0L;

        given(trainerService.deleteTrainerById(anyLong())).willThrow(
                new ResourceNotFoundException(String.format("Trainer with id: %s not found", trainerId)));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.NOT_FOUND)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(String.format("Trainer with id: %s not found", trainerId))
                .build();

        // when then
        mockMvc.perform(delete("/api/v1/trainers/{trainerId}", trainerId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }
}