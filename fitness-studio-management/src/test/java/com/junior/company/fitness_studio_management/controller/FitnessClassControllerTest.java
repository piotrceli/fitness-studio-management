package com.junior.company.fitness_studio_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junior.company.fitness_studio_management.dto.FitnessClassRequest;
import com.junior.company.fitness_studio_management.exception.ResourceNotFoundException;
import com.junior.company.fitness_studio_management.model.DifficultyLevel;
import com.junior.company.fitness_studio_management.model.FitnessClass;
import com.junior.company.fitness_studio_management.model.Response;
import com.junior.company.fitness_studio_management.security.AppUserDetailsService;
import com.junior.company.fitness_studio_management.service.FitnessClassService;
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

@WebMvcTest(FitnessClassController.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@WithMockUser(roles = {"ADMIN"})
class FitnessClassControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FitnessClassService fitnessClassService;

    @MockBean
    private AppUserDetailsService appUserDetailsService;

    @Test
    void shouldGetListOfFitnessClasses() throws Exception {

        // given
        FitnessClass fitnessClassOne = FitnessClass.builder()
                .id(1L)
                .name("name")
                .difficultyLevel(DifficultyLevel.BEGINNER)
                .description("description")
                .trainers(null)
                .build();

        FitnessClass fitnessClassTwo = FitnessClass.builder()
                .id(1L)
                .name("name")
                .difficultyLevel(DifficultyLevel.BEGINNER)
                .description("description")
                .trainers(null)
                .build();

        List<FitnessClass> fitnessClasses = List.of(fitnessClassOne, fitnessClassTwo);

        given(fitnessClassService.findAllFitnessClasses()).willReturn(fitnessClasses);

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Retrieved list of fitness classes")
                .data(Map.of("fitness_classes", fitnessClassService.findAllFitnessClasses()))
                .build();

        // when then
        mockMvc.perform(get("/api/v1/fitness-classes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    void shouldGetFitnessClassById_givenValidFitnessClassId() throws Exception {

        // given
        Long fitnessClassId = 1L;
        FitnessClass fitnessClass = FitnessClass.builder()
                .id(fitnessClassId)
                .name("name")
                .difficultyLevel(DifficultyLevel.BEGINNER)
                .description("description")
                .trainers(null)
                .build();

        given(fitnessClassService.findFitnessClassById(anyLong())).willReturn(fitnessClass);

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(String.format("Retrieved fitness class by id: %s", fitnessClassId))
                .data(Map.of("fitness_class", fitnessClassService.findFitnessClassById(fitnessClassId)))
                .build();

        // when then
        mockMvc.perform(get("/api/v1/fitness-classes/{fitnessClassId}", fitnessClassId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    void shouldNotGetFitnessClassById_givenInvalidFitnessClassId() throws Exception {

        // given
        Long fitnessClassId = 0L;

        given(fitnessClassService.findFitnessClassById(anyLong())).willThrow(
                new ResourceNotFoundException(String.format("Fitness class with id: %s not found", fitnessClassId)));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.NOT_FOUND)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(String.format("Fitness class with id: %s not found", fitnessClassId))
                .build();

        // when then
        mockMvc.perform(get("/api/v1/fitness-classes/{fitnessClassId}", fitnessClassId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    void shouldCreateFitnessClass_givenValidFitnessClassRequest() throws Exception {

        // given
        FitnessClassRequest fitnessClassRequest = FitnessClassRequest.builder()
                .id(null)
                .name("name")
                .difficultyLevel("BEGINNER")
                .description("description")
                .build();

        FitnessClass fitnessClass = FitnessClass.builder()
                .id(1L)
                .name("name")
                .difficultyLevel(DifficultyLevel.BEGINNER)
                .description("description")
                .trainers(null)
                .build();

        given(fitnessClassService.createFitnessClass(any())).willReturn(fitnessClass);

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .message("Created new fitness class")
                .data(Map.of("fitness_class", fitnessClassService.createFitnessClass(fitnessClassRequest)))
                .build();

        // when then
        mockMvc.perform(post("/api/v1/fitness-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fitnessClassRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    void shouldNotCreateFitnessClass_givenInvalidFitnessClassRequest() throws Exception {

        // given
        FitnessClassRequest fitnessClassRequest = FitnessClassRequest.builder()
                .id(null)
                .name("name")
                .difficultyLevel("BEGINNER")
                .description(null)
                .build();

        Map<String, String> errors = new HashMap<>();
        String fieldName1 = "description";
        String errorMessage1 = "Cannot be empty";
        errors.put(fieldName1, errorMessage1);
        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("error occurred")
                .data(Map.of("errors", errors))
                .build();

        // when then
        mockMvc.perform(post("/api/v1/fitness-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fitnessClassRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    void shouldUpdateFitnessClass_givenValidFitnessClassRequest() throws Exception{

        // given
        FitnessClassRequest fitnessClassRequest = FitnessClassRequest.builder()
                .id(1L)
                .name("name")
                .difficultyLevel("BEGINNER")
                .description("description")
                .build();

        FitnessClass fitnessClass = FitnessClass.builder()
                .id(1L)
                .name("name")
                .difficultyLevel(DifficultyLevel.BEGINNER)
                .description("description")
                .trainers(null)
                .build();

        given(fitnessClassService.updateFitnessClass(any())).willReturn(fitnessClass);

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Updated fitness class")
                .data(Map.of("fitness_class", fitnessClassService.updateFitnessClass(fitnessClassRequest)))
                .build();

        // when then
        mockMvc.perform(put("/api/v1/fitness-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fitnessClassRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    void shouldNotUpdateFitnessClass_givenInvalidFitnessClassRequestId() throws Exception {

        // given
        Long fitnessClassId = 0L;
        FitnessClassRequest fitnessClassRequest = FitnessClassRequest.builder()
                .id(fitnessClassId)
                .name("name")
                .difficultyLevel("BEGINNER")
                .description("description")
                .build();

        given(fitnessClassService.updateFitnessClass(any())).willThrow(
                new ResourceNotFoundException(String.format("Fitness class with id: %s not found", fitnessClassId)));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.NOT_FOUND)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(String.format("Fitness class with id: %s not found", fitnessClassId))
                .build();

        // when then
        mockMvc.perform(put("/api/v1/fitness-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fitnessClassRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    void shouldDeleteFitnessClassById_givenValidFitnessClassId() throws Exception{

        // given
        Long fitnessClassId = 1L;

        given(fitnessClassService.deleteFitnessClassById(anyLong())).willReturn(true);

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(String.format("Deleted fitness class with id: %s", fitnessClassId))
                .data(Map.of("is_deleted", fitnessClassService.deleteFitnessClassById(fitnessClassId)))
                .build();

        // when then
        mockMvc.perform(delete("/api/v1/fitness-classes/{fitnessClassId}", fitnessClassId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    void shouldNotDeleteFitnessClassById_givenInvalidFitnessClassId() throws Exception {

        // given
        Long fitnessClassId = 0L;

        given(fitnessClassService.deleteFitnessClassById(anyLong())).willThrow(
                new ResourceNotFoundException(String.format("Fitness class with id: %s not found", fitnessClassId)));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.NOT_FOUND)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(String.format("Fitness class with id: %s not found", fitnessClassId))
                .build();

        // when then
        mockMvc.perform(delete("/api/v1/fitness-classes/{fitnessClassId}", fitnessClassId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    void shouldAssignTrainerToFitnessClass_givenValidTrainerIdAndFitnessClassId() throws Exception {

        // given
        Long trainerId = 1L;
        Long fitnessClassId = 1L;

        given(fitnessClassService.assignTrainer(anyLong(), anyLong())).willReturn(true);

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(String.format("Assigned trainer with id: %s to fitness class with id: %s", trainerId, fitnessClassId))
                .data(Map.of("is_assigned", fitnessClassService.assignTrainer(fitnessClassId, trainerId)))
                .build();

        // when then
        mockMvc.perform(put("/api/v1/fitness-classes/assign/{fitnessClassId}/{trainerId}", fitnessClassId, trainerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    void shouldNotAssignTrainerToFitnessClass_givenInvalidFitnessClassId() throws Exception {

        // given
        Long trainerId = 1L;
        Long fitnessClassId = 0L;

        given(fitnessClassService.assignTrainer(anyLong(), anyLong())).willThrow(
                new ResourceNotFoundException(String.format("Fitness class with id: %s not found", fitnessClassId)));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.NOT_FOUND)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(String.format("Fitness class with id: %s not found", fitnessClassId))
                .build();

        // when then
        mockMvc.perform(put("/api/v1/fitness-classes/assign/{fitnessClassId}/{trainerId}", fitnessClassId, trainerId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    void shouldNotAssignTrainerToFitnessClass_givenInvalidTrainerId() throws Exception {

        // given
        Long trainerId = 0L;
        Long fitnessClassId = 1L;

        given(fitnessClassService.assignTrainer(anyLong(), anyLong())).willThrow(
                new ResourceNotFoundException(String.format("Trainer with id: %s not found", trainerId)));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.NOT_FOUND)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(String.format("Trainer with id: %s not found", trainerId))
                .build();

        // when then
        mockMvc.perform(put("/api/v1/fitness-classes/assign/{fitnessClassId}/{trainerId}", fitnessClassId, trainerId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    void shouldNotAssignTrainerToFitnessClass_whenTrainerIsAlreadyAssignedToFitnessClass() throws Exception {

        // given
        Long trainerId = 1L;
        Long fitnessClassId = 1L;

        given(fitnessClassService.assignTrainer(anyLong(), anyLong())).willThrow(
                new IllegalStateException(String.format(
                        "Trainer with id: %s is already assigned to fitness class with id: %s", trainerId, fitnessClassId)));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(String.format(
                        "Trainer with id: %s is already assigned to fitness class with id: %s", trainerId, fitnessClassId))
                .build();

        // when then
        mockMvc.perform(put("/api/v1/fitness-classes/assign/{fitnessClassId}/{trainerId}", fitnessClassId, trainerId))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    void shouldUnassignTrainerFromFitnessClass_givenValidTrainerIdAndFitnessClassId() throws Exception {

        // given
        Long trainerId = 1L;
        Long fitnessClassId = 1L;

        given(fitnessClassService.assignTrainer(anyLong(), anyLong())).willReturn(true);

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(String.format("Unassigned trainer with id: %s to fitness class with id: %s", trainerId, fitnessClassId))
                .data(Map.of("is_unassigned", fitnessClassService.unassignTrainer(fitnessClassId, trainerId)))
                .build();

        // when then
        mockMvc.perform(put("/api/v1/fitness-classes/unassign/{fitnessClassId}/{trainerId}", fitnessClassId, trainerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    void shouldNotUnassignTrainerToFitnessClass_givenInvalidFitnessClassId() throws Exception {

        // given
        Long trainerId = 1L;
        Long fitnessClassId = 0L;

        given(fitnessClassService.unassignTrainer(anyLong(), anyLong())).willThrow(
                new ResourceNotFoundException(String.format("Fitness class with id: %s not found", fitnessClassId)));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.NOT_FOUND)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(String.format("Fitness class with id: %s not found", fitnessClassId))
                .build();

        // when then
        mockMvc.perform(put("/api/v1/fitness-classes/unassign/{fitnessClassId}/{trainerId}", fitnessClassId, trainerId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    void shouldNotUnassignTrainerToFitnessClass_givenInvalidTrainerId() throws Exception {

        // given
        Long trainerId = 0L;
        Long fitnessClassId = 1L;

        given(fitnessClassService.unassignTrainer(anyLong(), anyLong())).willThrow(
                new ResourceNotFoundException(String.format("Trainer with id: %s not found", trainerId)));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.NOT_FOUND)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(String.format("Trainer with id: %s not found", trainerId))
                .build();

        // when then
        mockMvc.perform(put("/api/v1/fitness-classes/unassign/{fitnessClassId}/{trainerId}", fitnessClassId, trainerId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    void shouldNotUnassignTrainerFromFitnessClass_whenTrainerIsNotAssignedToFitnessClass() throws Exception {

        // given
        Long trainerId = 1L;
        Long fitnessClassId = 1L;

        given(fitnessClassService.unassignTrainer(anyLong(), anyLong())).willThrow(
                new IllegalStateException(String.format(
                        "Trainer with id: %s is not assigned to fitness class with id: %s", trainerId, fitnessClassId)));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(String.format(
                        "Trainer with id: %s is not assigned to fitness class with id: %s", trainerId, fitnessClassId))
                .build();

        // when then
        mockMvc.perform(put("/api/v1/fitness-classes/unassign/{fitnessClassId}/{trainerId}", fitnessClassId, trainerId))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }
}