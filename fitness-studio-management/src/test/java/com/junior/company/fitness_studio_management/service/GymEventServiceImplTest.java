package com.junior.company.fitness_studio_management.service;

import com.junior.company.fitness_studio_management.dto.GymEventRequest;
import com.junior.company.fitness_studio_management.dto.GymEventResponse;
import com.junior.company.fitness_studio_management.exception.InvalidGymEventDateException;
import com.junior.company.fitness_studio_management.exception.ResourceNotFoundException;
import com.junior.company.fitness_studio_management.mapper.GymEventMapper;
import com.junior.company.fitness_studio_management.model.AppUser;
import com.junior.company.fitness_studio_management.model.DifficultyLevel;
import com.junior.company.fitness_studio_management.model.FitnessClass;
import com.junior.company.fitness_studio_management.model.GymEvent;
import com.junior.company.fitness_studio_management.repository.FitnessClassRepository;
import com.junior.company.fitness_studio_management.repository.GymEventRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
class GymEventServiceImplTest {

    @Mock
    private GymEventRepository gymEventRepository;

    @Mock
    private FitnessClassRepository fitnessClassRepository;

    @Mock
    private AppUserServiceImpl appUserService;

    @InjectMocks
    private GymEventServiceImpl gymEventService;

    @Test
    void shouldGetListOfGymEvents() {

        // when
        gymEventService.findAllGymEvents();

        // then
        verify(gymEventRepository, times(1)).findAll(Sort.by("startTime"));
    }

    @Test
    void shouldFindGymEventById_givenValidGymEventId() {

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

        given(gymEventRepository.findById(anyLong())).willReturn(Optional.of(gymEvent));

        GymEventResponse gymEventResponse = GymEventMapper.mapGymEventToGymEventResponse(gymEvent);

        // when
        GymEventResponse result = gymEventService.findGymEventById(gymEventId);

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(gymEventResponse);
    }

    @Test
    void shouldNotFindGymEventById_givenInvalidGymEventId() {

        // given
        Long gymEventId = 0L;

        // when then
        assertThatThrownBy(() -> gymEventService.findGymEventById(gymEventId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("Gym event with id: %s not found", gymEventId));
    }

    @Test
    void shouldFindGymEventByIdWithParticipants_givenValidGymEventId() {

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
                .participantsLimit(20)
                .currentParticipantsNumber(0)
                .enrolledParticipants(new ArrayList<>())
                .fitnessClass(fitnessClass)
                .build();

        given(gymEventRepository.findById(anyLong())).willReturn(Optional.of(gymEvent));

        GymEventResponse gymEventResponse = GymEventMapper.mapGymEventToGymEventResponseMng(gymEvent);

        // when
        GymEventResponse result = gymEventService.findGymEventByIdWithParticipants(gymEventId);

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(gymEventResponse);
    }

    @Test
    void shouldNotFindGymEventByIdWithParticipants_givenInvalidGymEventId() {

        // given
        Long gymEventId = 0L;

        // when then
        assertThatThrownBy(() -> gymEventService.findGymEventByIdWithParticipants(gymEventId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("Gym event with id: %s not found", gymEventId));
    }

    @Test
    void shouldCreateGymEvent_givenValidGymEventRequest() {

        // given
        FitnessClass fitnessClass = FitnessClass.builder()
                .id(1L)
                .name("name")
                .difficultyLevel(DifficultyLevel.BEGINNER)
                .description("description")
                .trainers(null)
                .build();

        GymEventRequest gymEventRequest = GymEventRequest.builder()
                .startTime(LocalDateTime.of(5000, 1, 1, 12, 0, 0))
                .endTime(LocalDateTime.of(5000, 1, 1, 13, 0, 0))
                .fitnessClassId(1L)
                .build();

        given(fitnessClassRepository.findById(gymEventRequest.getFitnessClassId())).willReturn(Optional.of(fitnessClass));

        GymEvent gymEvent = GymEventMapper.mapGymEventRequestToGymEvent(gymEventRequest);
        gymEvent.setDuration("01:00");
        gymEvent.setFitnessClass(fitnessClass);

        given(gymEventRepository.save(any())).willReturn(gymEvent);

        // when
        GymEvent result = gymEventService.createGymEvent(gymEventRequest);

        // then
        ArgumentCaptor<GymEvent> gymEventArgumentCaptor = ArgumentCaptor.forClass(GymEvent.class);
        verify(gymEventRepository).save(gymEventArgumentCaptor.capture());
        GymEvent capturedGymEvent = gymEventArgumentCaptor.getValue();

        assertThat(capturedGymEvent).usingRecursiveComparison().isEqualTo(gymEvent);
        assertThat(result).isEqualTo(gymEvent);
    }

    @Test
    void shouldNotCreateGymEvent_givenInvalidGymEventRequestFitnessClassId() {

        // given
        GymEventRequest gymEventRequest = GymEventRequest.builder()
                .startTime(LocalDateTime.of(5000, 1, 1, 12, 0, 0))
                .endTime(LocalDateTime.of(5000, 1, 1, 13, 0, 0))
                .fitnessClassId(1L)
                .build();

        given(fitnessClassRepository.findById(gymEventRequest.getFitnessClassId())).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> gymEventService.createGymEvent(gymEventRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("Fitness class with id: %s not found", gymEventRequest.getFitnessClassId()));
    }

    @Test
    void shouldNotCreateGymEvent_givenInvalidGymEventRequestStartDate() {

        // given
        GymEventRequest gymEventRequest = GymEventRequest.builder()
                .startTime(LocalDateTime.of(6000, 1, 1, 12, 0, 0))
                .endTime(LocalDateTime.of(5000, 1, 1, 13, 0, 0))
                .fitnessClassId(1L)
                .build();

        // when then
        assertThatThrownBy(() -> gymEventService.createGymEvent(gymEventRequest))
                .isInstanceOf(InvalidGymEventDateException.class)
                .hasMessageContaining("Entered dates are not valid");
    }

    @Test
    void shouldDeleteGymEventById_givenValidGymEventId() {

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

        given(gymEventRepository.findById(anyLong())).willReturn(Optional.of(gymEvent));

        // when
        boolean result = gymEventService.deleteGymEventById(gymEventId);

        // then
        assertThat(result).isTrue();
        verify(gymEventRepository, times(1)).delete(gymEvent);
    }

    @Test
    void shouldNotDeleteGymEventById_givenInvalidGymEventId() {

        // given
        Long gymEventId = 0L;
        GymEvent gymEvent = GymEvent.builder()
                .id(gymEventId)
                .startTime(LocalDateTime.of(2000, 1, 1, 12, 0, 0))
                .endTime(LocalDateTime.of(2000, 1, 1, 13, 0, 0))
                .duration("01:00")
                .fitnessClass(null)
                .build();

        given(gymEventRepository.findById(anyLong())).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> gymEventService.deleteGymEventById(gymEventId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("Gym event with id: %s not found", gymEventId));
        verify(gymEventRepository, never()).delete(gymEvent);
    }

    @Test
    void shouldEnrollEvent_givenValidGymEventId() {

        // when
        Long gymEventId = 1L;

        List<GymEvent> participantGymEventList = new ArrayList<>();
        AppUser participant = AppUser.builder()
                .id(2L)
                .username("participant")
                .password("password_one")
                .roles(new ArrayList<>())
                .firstName("firstname_one")
                .lastName("lastname_one")
                .email("one@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .isEnabled(true)
                .gymEvents(participantGymEventList)
                .build();

        List<AppUser> gymEventParticipantsList = new ArrayList<>();
        GymEvent gymEvent = GymEvent.builder()
                .id(gymEventId)
                .startTime(LocalDateTime.of(3000, 1, 1, 12, 0, 0))
                .endTime(LocalDateTime.of(3000, 1, 1, 13, 0, 0))
                .duration("01:00")
                .participantsLimit(20)
                .currentParticipantsNumber(0)
                .enrolledParticipants(gymEventParticipantsList)
                .fitnessClass(null)
                .build();

        participantGymEventList.add(gymEvent);
        gymEventParticipantsList.add(participant);

        AppUser appUser = AppUser.builder()
                .id(1L)
                .username("user")
                .password("password_one")
                .roles(new ArrayList<>())
                .firstName("firstname_one")
                .lastName("lastname_one")
                .email("one@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .isEnabled(true)
                .gymEvents(new ArrayList<>())
                .build();

        given(gymEventRepository.findById(gymEventId)).willReturn(Optional.of(gymEvent));

        given(appUserService.getCurrentUser()).willReturn(appUser);

        // when
        boolean result = gymEventService.enrollUser(gymEventId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void shouldNotEnrollEvent_givenInvalidGymEventId() {

        // when
        Long gymEventId = 0L;

        given(gymEventRepository.findById(gymEventId)).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> gymEventService.enrollUser(gymEventId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("Gym event with id: %s not found", gymEventId));
    }

    @Test
    void shouldNotEnrollEvent_whenGymEventIsFull_givenValidGymEventId() {

        // when
        Long gymEventId = 1L;
        GymEvent gymEvent = GymEvent.builder()
                .id(gymEventId)
                .startTime(LocalDateTime.of(3000, 1, 1, 12, 0, 0))
                .endTime(LocalDateTime.of(3000, 1, 1, 13, 0, 0))
                .duration("01:00")
                .participantsLimit(20)
                .currentParticipantsNumber(20)
                .enrolledParticipants(new ArrayList<>())
                .fitnessClass(null)
                .build();

        given(gymEventRepository.findById(gymEventId)).willReturn(Optional.of(gymEvent));

        // when
        boolean result = gymEventService.enrollUser(gymEventId);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void shouldNotEnrollEvent_whenGymEventParticipantsLimitIsZero_givenValidGymEventId() {

        // when
        Long gymEventId = 1L;
        GymEvent gymEvent = GymEvent.builder()
                .id(gymEventId)
                .startTime(LocalDateTime.of(3000, 1, 1, 12, 0, 0))
                .endTime(LocalDateTime.of(3000, 1, 1, 13, 0, 0))
                .duration("01:00")
                .participantsLimit(0)
                .currentParticipantsNumber(0)
                .enrolledParticipants(new ArrayList<>())
                .fitnessClass(null)
                .build();

        given(gymEventRepository.findById(gymEventId)).willReturn(Optional.of(gymEvent));

        // when
        boolean result = gymEventService.enrollUser(gymEventId);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void shouldNotEnrollEvent_whenGymEventStartTimeIsInvalid_givenValidGymEventId() {

        // when
        Long gymEventId = 1L;
        GymEvent gymEvent = GymEvent.builder()
                .id(gymEventId)
                .startTime(LocalDateTime.of(1000, 1, 1, 12, 0, 0))
                .endTime(LocalDateTime.of(1000, 1, 1, 13, 0, 0))
                .duration("01:00")
                .participantsLimit(10)
                .currentParticipantsNumber(0)
                .enrolledParticipants(new ArrayList<>())
                .fitnessClass(null)
                .build();

        AppUser appUser = AppUser.builder()
                .id(1L)
                .username("user")
                .password("password_one")
                .roles(new ArrayList<>())
                .firstName("firstname_one")
                .lastName("lastname_one")
                .email("one@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .isEnabled(true)
                .gymEvents(new ArrayList<>())
                .build();

        given(gymEventRepository.findById(gymEventId)).willReturn(Optional.of(gymEvent));
        given(appUserService.getCurrentUser()).willReturn(appUser);

        // when
        boolean result = gymEventService.enrollUser(gymEventId);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void shouldNotEnrollEvent_whenCurrentUserIsAlreadyEnrolled_givenValidGymEventId() {

        // when
        Long gymEventId = 1L;

        AppUser appUser = AppUser.builder()
                .id(1L)
                .username("user")
                .password("password_one")
                .roles(new ArrayList<>())
                .firstName("firstname_one")
                .lastName("lastname_one")
                .email("one@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .isEnabled(true)
                .gymEvents(new ArrayList<>())
                .build();

        GymEvent gymEvent = GymEvent.builder()
                .id(gymEventId)
                .startTime(LocalDateTime.of(2000, 1, 1, 12, 0, 0))
                .endTime(LocalDateTime.of(2000, 1, 1, 13, 0, 0))
                .duration("01:00")
                .participantsLimit(20)
                .currentParticipantsNumber(0)
                .enrolledParticipants(List.of(appUser))
                .fitnessClass(null)
                .build();

        given(gymEventRepository.findById(gymEventId)).willReturn(Optional.of(gymEvent));

        given(appUserService.getCurrentUser()).willReturn(appUser);

        // when
        boolean result = gymEventService.enrollUser(gymEventId);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void shouldDisenrollEvent_givenValidGymEventId() {

        // when
        Long gymEventId = 1L;

        List<GymEvent> participantGymEventList = new ArrayList<>();
        AppUser appUser = AppUser.builder()
                .id(2L)
                .username("participant")
                .password("password_one")
                .roles(new ArrayList<>())
                .firstName("firstname_one")
                .lastName("lastname_one")
                .email("one@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .isEnabled(true)
                .gymEvents(participantGymEventList)
                .build();

        List<AppUser> gymEventParticipantsList = new ArrayList<>();
        GymEvent gymEvent = GymEvent.builder()
                .id(gymEventId)
                .startTime(LocalDateTime.of(3000, 1, 1, 12, 0, 0))
                .endTime(LocalDateTime.of(3000, 1, 1, 13, 0, 0))
                .duration("01:00")
                .participantsLimit(20)
                .currentParticipantsNumber(1)
                .enrolledParticipants(gymEventParticipantsList)
                .fitnessClass(null)
                .build();

        participantGymEventList.add(gymEvent);
        gymEventParticipantsList.add(appUser);

        given(gymEventRepository.findById(gymEventId)).willReturn(Optional.of(gymEvent));
        given(appUserService.getCurrentUser()).willReturn(appUser);

        // when
        boolean result = gymEventService.disenrollUser(gymEventId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void shouldNotDisenrollEvent_givenInvalidGymEventId() {

        // when
        Long gymEventId = 0L;

        given(gymEventRepository.findById(gymEventId)).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> gymEventService.disenrollUser(gymEventId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("Gym event with id: %s not found", gymEventId));
    }

    @Test
    void shouldNotDisenrollEvent_whenGymEventStartTimeIsInvalid_givenValidGymEventId() {

        // when
        Long gymEventId = 1L;

        List<GymEvent> participantGymEventList = new ArrayList<>();
        AppUser appUser = AppUser.builder()
                .id(2L)
                .username("participant")
                .password("password_one")
                .roles(new ArrayList<>())
                .firstName("firstname_one")
                .lastName("lastname_one")
                .email("one@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .isEnabled(true)
                .gymEvents(participantGymEventList)
                .build();

        List<AppUser> gymEventParticipantsList = new ArrayList<>();
        GymEvent gymEvent = GymEvent.builder()
                .id(gymEventId)
                .startTime(LocalDateTime.of(1000, 1, 1, 12, 0, 0))
                .endTime(LocalDateTime.of(1000, 1, 1, 13, 0, 0))
                .duration("01:00")
                .participantsLimit(20)
                .currentParticipantsNumber(1)
                .enrolledParticipants(gymEventParticipantsList)
                .fitnessClass(null)
                .build();

        participantGymEventList.add(gymEvent);
        gymEventParticipantsList.add(appUser);

        given(gymEventRepository.findById(gymEventId)).willReturn(Optional.of(gymEvent));
        given(appUserService.getCurrentUser()).willReturn(appUser);

        // when
        boolean result = gymEventService.disenrollUser(gymEventId);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void shouldNotDisenrollEvent_whenCurrentUserIsNotEnrolledToGymEvent_givenValidGymEventId() {

        // when
        Long gymEventId = 1L;

        List<GymEvent> participantGymEventList = new ArrayList<>();
        AppUser participant = AppUser.builder()
                .id(2L)
                .username("participant")
                .password("password_one")
                .roles(new ArrayList<>())
                .firstName("firstname_one")
                .lastName("lastname_one")
                .email("one@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .isEnabled(true)
                .gymEvents(participantGymEventList)
                .build();

        List<AppUser> gymEventParticipantsList = new ArrayList<>();
        GymEvent gymEvent = GymEvent.builder()
                .id(gymEventId)
                .startTime(LocalDateTime.of(2000, 1, 1, 12, 0, 0))
                .endTime(LocalDateTime.of(2000, 1, 1, 13, 0, 0))
                .duration("01:00")
                .participantsLimit(20)
                .currentParticipantsNumber(1)
                .enrolledParticipants(gymEventParticipantsList)
                .fitnessClass(null)
                .build();

        participantGymEventList.add(gymEvent);
        gymEventParticipantsList.add(participant);

        AppUser appUser = AppUser.builder()
                .id(1L)
                .username("participant")
                .password("password_one")
                .roles(new ArrayList<>())
                .firstName("firstname_one")
                .lastName("lastname_one")
                .email("one@email.com")
                .dob(LocalDate.of(2000, 1, 1))
                .isEnabled(true)
                .gymEvents(new ArrayList<>())
                .build();

        given(gymEventRepository.findById(gymEventId)).willReturn(Optional.of(gymEvent));

        given(appUserService.getCurrentUser()).willReturn(appUser);

        // when
        boolean result = gymEventService.disenrollUser(gymEventId);

        // then
        assertThat(result).isFalse();
    }
}