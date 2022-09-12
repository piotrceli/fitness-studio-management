package com.junior.company.fitness_studio_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junior.company.fitness_studio_management.dto.GymEventRequest;
import com.junior.company.fitness_studio_management.exception.ResourceNotFoundException;
import com.junior.company.fitness_studio_management.mapper.GymEventMapper;
import com.junior.company.fitness_studio_management.model.DifficultyLevel;
import com.junior.company.fitness_studio_management.model.FitnessClass;
import com.junior.company.fitness_studio_management.model.GymEvent;
import com.junior.company.fitness_studio_management.model.Response;
import com.junior.company.fitness_studio_management.security.AppUserDetailsService;
import com.junior.company.fitness_studio_management.service.GymEventService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GymEventController.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
class GymEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GymEventService gymEventService;

    @MockBean
    private AppUserDetailsService appUserDetailsService;

    @Test
    void shouldFindAllGymEvents() throws Exception {

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

        GymEvent gymEventOne = GymEvent.builder()
                .id(1L)
                .startTime(LocalDateTime.of(2000, 1, 1, 12, 0, 0))
                .endTime(LocalDateTime.of(2000, 1, 1, 13, 0, 0))
                .duration("01:00")
                .fitnessClass(fitnessClassOne)
                .build();

        GymEvent gymEventTwo = GymEvent.builder()
                .id(2L)
                .startTime(LocalDateTime.of(2000, 1, 1, 14, 0, 0))
                .endTime(LocalDateTime.of(2000, 1, 1, 15, 30, 0))
                .duration("01:30")
                .fitnessClass(fitnessClassTwo)
                .build();

        List<GymEvent> gymEvents = List.of(gymEventOne, gymEventTwo);

        given(gymEventService.findAllGymEvents())
                .willReturn(GymEventMapper.mapGymEventListToGymEventResponseList(gymEvents));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Retrieved list of gym events")
                .data(Map.of("gym_events", gymEventService.findAllGymEvents()))
                .build();

        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/gym-events"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    void shouldGetGymEventById_givenValidGymEventId() throws Exception {

        // given
        Long gymEventId = 1L;
        FitnessClass fitnessClass = FitnessClass.builder()
                .id(1L)
                .name("name")
                .difficultyLevel(DifficultyLevel.BEGINNER)
                .description("description")
                .trainers(null)
                .build();

        GymEvent gymEvent = GymEvent.builder()
                .id(gymEventId)
                .startTime(LocalDateTime.of(2000, 1, 1, 12, 0, 0))
                .endTime(LocalDateTime.of(2000, 1, 1, 13, 0, 0))
                .duration("01:00")
                .fitnessClass(fitnessClass)
                .build();

        given(gymEventService.findGymEventById(anyLong())).willReturn(
                GymEventMapper.mapGymEventToGymEventResponse(gymEvent));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(String.format("Retrieved gym event by id: %s", gymEventId))
                .data(Map.of("gym_event", gymEventService.findGymEventById(gymEventId)))
                .build();

        // when then
        mockMvc.perform(get("/api/v1/gym-events/{gymEventId}", gymEventId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    void shouldNotGetGymEventById_givenInvalidGymEventId() throws Exception {

        // given
        Long gymEventId = 0L;

        given(gymEventService.findGymEventById(anyLong())).willThrow(
                new ResourceNotFoundException(String.format("Gym event with id: %s not found", gymEventId)));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.NOT_FOUND)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(String.format("Gym event with id: %s not found", gymEventId))
                .build();

        // when then
        mockMvc.perform(get("/api/v1/gym-events/{gymEventId}", gymEventId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldGetGymEventByIdWithParticipants_givenValidGymEventId() throws Exception {

        // given
        Long gymEventId = 1L;
        FitnessClass fitnessClass = FitnessClass.builder()
                .id(1L)
                .name("name")
                .difficultyLevel(DifficultyLevel.BEGINNER)
                .description("description")
                .trainers(null)
                .build();

        GymEvent gymEvent = GymEvent.builder()
                .id(gymEventId)
                .startTime(LocalDateTime.of(2000, 1, 1, 12, 0, 0))
                .endTime(LocalDateTime.of(2000, 1, 1, 13, 0, 0))
                .duration("01:00")
                .enrolledParticipants(new ArrayList<>())
                .fitnessClass(fitnessClass)
                .build();

        given(gymEventService.findGymEventByIdWithParticipants(anyLong())).willReturn(
                GymEventMapper.mapGymEventToGymEventResponseMng(gymEvent));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(String.format("Retrieved gym event by id: %s with participants", gymEventId))
                .data(Map.of("gym_event", gymEventService.findGymEventByIdWithParticipants(gymEventId)))
                .build();

        // when then
        mockMvc.perform(get("/api/v1/gym-events/mng/{gymEventId}", gymEventId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldNotGetGymEventByIdWithParticipants_givenInvalidGymEventId() throws Exception {

        // given
        Long gymEventId = 0L;

        given(gymEventService.findGymEventByIdWithParticipants(anyLong())).willThrow(
                new ResourceNotFoundException(String.format("Gym event with id: %s not found", gymEventId)));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.NOT_FOUND)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(String.format("Gym event with id: %s not found", gymEventId))
                .build();

        // when then
        mockMvc.perform(get("/api/v1/gym-events/mng/{gymEventId}", gymEventId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldCreateGymEvent_givenValidGymEventRequest() throws Exception {

        // given
        FitnessClass fitnessClass = FitnessClass.builder()
                .id(1L)
                .name("name")
                .difficultyLevel(DifficultyLevel.BEGINNER)
                .description("description")
                .trainers(null)
                .build();

        GymEventRequest gymEventRequest = GymEventRequest.builder()
                .startTime(LocalDateTime.of(2000, 1, 1, 12, 0, 0))
                .endTime(LocalDateTime.of(2000, 1, 1, 13, 0, 0))
                .participantsLimit(10)
                .fitnessClassId(1L)
                .build();

        GymEvent gymEvent = GymEvent.builder()
                .id(1L)
                .startTime(LocalDateTime.of(2000, 1, 1, 12, 0, 0))
                .endTime(LocalDateTime.of(2000, 1, 1, 13, 0, 0))
                .duration("01:00")
                .participantsLimit(10)
                .currentParticipantsNumber(0)
                .enrolledParticipants(null)
                .fitnessClass(fitnessClass)
                .build();

        given(gymEventService.createGymEvent(any())).willReturn(gymEvent);

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .message("Created new gym event")
                .data(Map.of("gym_event", gymEventService.createGymEvent(gymEventRequest)))
                .build();

        // when then
        mockMvc.perform(post("/api/v1/gym-events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gymEventRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldNotCreateGymEvent_givenInvalidGymEventRequest() throws Exception {

        // given
        GymEventRequest gymEventRequest = GymEventRequest.builder()
                .startTime(LocalDateTime.of(2000, 1, 1, 12, 0, 0))
                .endTime(LocalDateTime.of(2000, 1, 1, 13, 0, 0))
                .fitnessClassId(null)
                .build();

        Map<String, String> errors = new HashMap<>();
        String fieldName = "fitnessClassId";
        String errorMessage = "Cannot be empty";
        errors.put(fieldName, errorMessage);
        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("error occurred")
                .data(Map.of("errors", errors))
                .build();

        // when then
        mockMvc.perform(post("/api/v1/gym-events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gymEventRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldDeleteGymEventById_givenValidGymEventId() throws Exception {

        // given
        Long gymEventId = 1L;

        given(gymEventService.deleteGymEventById(anyLong())).willReturn(true);

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(String.format("Deleted gym event with id: %s", gymEventId))
                .data(Map.of("is_deleted", gymEventService.deleteGymEventById(gymEventId)))
                .build();

        // when then
        mockMvc.perform(delete("/api/v1/gym-events/{gymEventId}", gymEventId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldNotDeleteGymEventById_givenInvalidGymEventId() throws Exception {

        // given
        Long gymEventId = 0L;

        given(gymEventService.deleteGymEventById(anyLong())).willThrow(
                new ResourceNotFoundException(String.format("Gym event with id: %s not found", gymEventId)));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.NOT_FOUND)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(String.format("Gym event with id: %s not found", gymEventId))
                .build();

        // when then
        mockMvc.perform(delete("/api/v1/gym-events/{gymEventId}", gymEventId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void shouldEnrollInEvent_givenValidGymEventId() throws Exception {

        // given
        Long gymEventId = 1L;

        given(gymEventService.enrollUser(gymEventId)).willReturn(true);

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Enrolled in event")
                .data(Map.of("is_enrolled", gymEventService.enrollUser(gymEventId)))
                .build();

        // when then
        mockMvc.perform(post("/api/v1/gym-events/enroll/{gymEventId}", gymEventId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void shouldNotEnrollInEvent_givenInvalidGymEventId() throws Exception {

        // given
        Long gymEventId = 0L;

        given(gymEventService.enrollUser(gymEventId)).willThrow(
                new ResourceNotFoundException(String.format("Gym event with id: %s not found", gymEventId)));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.NOT_FOUND)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(String.format("Gym event with id: %s not found", gymEventId))
                .build();

        // when then
        mockMvc.perform(post("/api/v1/gym-events/enroll/{gymEventId}", gymEventId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void shouldNotEnrollInEvent_whenFoundGymEventIsFull() throws Exception {

        // given
        Long gymEventId = 1L;

        given(gymEventService.enrollUser(gymEventId)).willReturn(false);

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Enrolled in event")
                .data(Map.of("is_enrolled", gymEventService.enrollUser(gymEventId)))
                .build();

        // when then
        mockMvc.perform(post("/api/v1/gym-events/enroll/{gymEventId}", gymEventId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void shouldDisenrollFromEvent_givenValidGymEventId() throws Exception {

        // given
        Long gymEventId = 1L;

        given(gymEventService.disenrollUser(gymEventId)).willReturn(true);

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Disenrolled from event")
                .data(Map.of("is_disenrolled", gymEventService.disenrollUser(gymEventId)))
                .build();

        // when then
        mockMvc.perform(post("/api/v1/gym-events/disenroll/{gymEventId}", gymEventId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void shouldNotDisenrollFromEvent_givenInvalidGymEventId() throws Exception {

        // given
        Long gymEventId = 0L;

        given(gymEventService.disenrollUser(gymEventId)).willThrow(
                new ResourceNotFoundException(String.format("Gym event with id: %s not found", gymEventId)));

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.NOT_FOUND)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(String.format("Gym event with id: %s not found", gymEventId))
                .build();

        // when then
        mockMvc.perform(post("/api/v1/gym-events/disenroll/{gymEventId}", gymEventId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void shouldNotDisenrollFromEvent_whenFoundGymStartTimeIsInvalid() throws Exception {

        // given
        Long gymEventId = 1L;

        given(gymEventService.disenrollUser(gymEventId)).willReturn(false);

        Response expectedResponseBody = Response.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Disenrolled from event")
                .data(Map.of("is_disenrolled", gymEventService.disenrollUser(gymEventId)))
                .build();

        // when then
        mockMvc.perform(post("/api/v1/gym-events/disenroll/{gymEventId}", gymEventId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponseBody)));
    }
}