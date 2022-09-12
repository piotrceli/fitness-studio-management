package com.junior.company.fitness_studio_management.service;

import com.junior.company.fitness_studio_management.dto.FitnessClassRequest;
import com.junior.company.fitness_studio_management.exception.ResourceNotFoundException;
import com.junior.company.fitness_studio_management.mapper.FitnessClassMapper;
import com.junior.company.fitness_studio_management.model.DifficultyLevel;
import com.junior.company.fitness_studio_management.model.FitnessClass;
import com.junior.company.fitness_studio_management.model.Trainer;
import com.junior.company.fitness_studio_management.repository.FitnessClassRepository;
import com.junior.company.fitness_studio_management.repository.TrainerRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
class FitnessClassServiceImplTest {

    @Mock
    private FitnessClassRepository fitnessClassRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private FitnessClassServiceImpl fitnessClassService;

    @Test
    void shouldGetListOfFitnessClasses() {

        // when
        fitnessClassService.findAllFitnessClasses();

        // then
        verify(fitnessClassRepository, times(1)).findAll();
    }

    @Test
    void shouldFindFitnessClassById_givenValidFitnessClassId() {

        // given
        Long fitnessClassId = 1L;

        FitnessClass fitnessClass = FitnessClass.builder()
                .id(fitnessClassId)
                .name("name")
                .difficultyLevel(DifficultyLevel.BEGINNER)
                .description("description")
                .trainers(null)
                .build();

        given(fitnessClassRepository.findById(fitnessClassId)).willReturn(Optional.of(fitnessClass));

        // when
        FitnessClass result = fitnessClassService.findFitnessClassById(fitnessClassId);

        // then
        assertThat(result).isEqualTo(fitnessClass);
    }

    @Test
    void shouldNotFindFitnessClassById_givenInvalidFitnessClassId() {

        // given
        Long fitnessClassId = 0L;

        // when then
        assertThatThrownBy(() -> fitnessClassService.findFitnessClassById(fitnessClassId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("Fitness class with id: %s not found", fitnessClassId));
    }

    @Test
    void shouldCreateFitnessClass_givenValidFitnessClassRequest() {

        // given
        FitnessClassRequest fitnessClassRequest = FitnessClassRequest.builder()
                .id(null)
                .name("name")
                .difficultyLevel("BEGINNER")
                .description("description")
                .build();

        FitnessClass fitnessClass = FitnessClassMapper.mapFitnessClassRequestToFitnessClassCreate(fitnessClassRequest);

        given(fitnessClassRepository.save(any())).willReturn(fitnessClass);

        // when
        FitnessClass result = fitnessClassService.createFitnessClass(fitnessClassRequest);

        // then
        ArgumentCaptor<FitnessClass> fitnessClassArgumentCaptor = ArgumentCaptor.forClass(FitnessClass.class);
        verify(fitnessClassRepository).save(fitnessClassArgumentCaptor.capture());
        FitnessClass capturedFitnessClass = fitnessClassArgumentCaptor.getValue();

        assertThat(capturedFitnessClass).usingRecursiveComparison().isEqualTo(fitnessClass);
        assertThat(result).isEqualTo(fitnessClass);
    }

    @Test
    void shouldUpdateFitnessClass_givenValidFitnessClassRequest() {

        // given
        FitnessClassRequest fitnessClassRequest = FitnessClassRequest.builder()
                .id(1L)
                .name("name")
                .difficultyLevel("BEGINNER")
                .description("description")
                .build();

        FitnessClass fitnessClass = FitnessClassMapper.mapFitnessClassRequestToFitnessClassUpdate(fitnessClassRequest, null);

        given(fitnessClassRepository.findById(anyLong())).willReturn(Optional.of(fitnessClass));
        given(fitnessClassRepository.save(any())).willReturn(fitnessClass);


        // when
        FitnessClass result = fitnessClassService.updateFitnessClass(fitnessClassRequest);

        // then
        ArgumentCaptor<FitnessClass> fitnessClassArgumentCaptor = ArgumentCaptor.forClass(FitnessClass.class);
        verify(fitnessClassRepository).save(fitnessClassArgumentCaptor.capture());
        FitnessClass capturedFitnessClass = fitnessClassArgumentCaptor.getValue();

        assertThat(capturedFitnessClass).usingRecursiveComparison().isEqualTo(fitnessClass);
        assertThat(result).isEqualTo(fitnessClass);
    }

    @Test
    void shouldNotUpdateFitnessClass_givenInvalidFitnessClassRequest() {

        // given
        FitnessClassRequest fitnessClassRequest = FitnessClassRequest.builder()
                .id(0L)
                .name("name")
                .difficultyLevel("BEGINNER")
                .description("description")
                .build();

        given(fitnessClassRepository.findById(anyLong())).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> fitnessClassService.updateFitnessClass(fitnessClassRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("Fitness class with id: %s not found", fitnessClassRequest.getId()));
    }

    @Test
    void shouldDeleteFitnessClassById_givenValidFitnessClassId() {

        // given
        Long fitnessClassId = 1L;

        FitnessClass fitnessClass = FitnessClass.builder()
                .id(fitnessClassId)
                .name("name")
                .difficultyLevel(DifficultyLevel.BEGINNER)
                .description("description")
                .trainers(null)
                .build();

        given(fitnessClassRepository.findById(anyLong())).willReturn(Optional.of(fitnessClass));

        // when
        boolean result = fitnessClassService.deleteFitnessClassById(fitnessClassId);

        // then
        assertThat(result).isTrue();
        verify(fitnessClassRepository, times(1)).delete(fitnessClass);

    }

    @Test
    void shouldNotDeleteFitnessClassById_givenInvalidFitnessClassId() {

        // given
        Long fitnessClassId = 0L;

        given(fitnessClassRepository.findById(anyLong())).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> fitnessClassService.deleteFitnessClassById(fitnessClassId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("Fitness class with id: %s not found", fitnessClassId));
    }

    @Test
    void shouldAssignTrainerToFitnessClass_givenValidTrainerIdAndFitnessClassId() {

        // given
        Long trainerId = 1L;
        Long fitnessClassId = 1L;

        Trainer trainer = Trainer.builder()
                .id(trainerId)
                .firstName("first_name")
                .lastName("last_name")
                .email("email@email.com")
                .description("description")
                .build();

        FitnessClass fitnessClass = FitnessClass.builder()
                .id(fitnessClassId)
                .name("name")
                .difficultyLevel(DifficultyLevel.BEGINNER)
                .description("description")
                .trainers(new ArrayList<>())
                .build();

        given(fitnessClassRepository.findById(fitnessClassId)).willReturn(Optional.of(fitnessClass));
        given(trainerRepository.findById(trainerId)).willReturn(Optional.of(trainer));

        // when
        boolean result = fitnessClassService.assignTrainer(fitnessClassId, trainerId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void shouldNotAssignTrainerToFitnessClass_whenTrainerIsAlreadyAssignedToFitnessClass() {

        // given
        Long trainerId = 1L;
        Long fitnessClassId = 1L;

        Trainer trainer = Trainer.builder()
                .id(trainerId)
                .firstName("first_name")
                .lastName("last_name")
                .email("email@email.com")
                .description("description")
                .build();

        FitnessClass fitnessClass = FitnessClass.builder()
                .id(fitnessClassId)
                .name("name")
                .difficultyLevel(DifficultyLevel.BEGINNER)
                .description("description")
                .trainers(List.of(trainer))
                .build();

        given(fitnessClassRepository.findById(fitnessClassId)).willReturn(Optional.of(fitnessClass));
        given(trainerRepository.findById(trainerId)).willReturn(Optional.of(trainer));

        // when then
        assertThatThrownBy(() -> fitnessClassService.assignTrainer(fitnessClassId, trainerId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format(
                        "Trainer with id: %s is already assigned to the fitness class with id: %s", trainerId, fitnessClassId));
    }

    @Test
    void shouldNotAssignTrainerToFitnessClass_givenInvalidFitnessClassId() {

        // given
        Long trainerId = 1L;
        Long fitnessClassId = 0L;

        given(fitnessClassRepository.findById(fitnessClassId)).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> fitnessClassService.assignTrainer(fitnessClassId, trainerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("Fitness class with id: %s not found", fitnessClassId));
    }

    @Test
    void shouldNotAssignTrainerToFitnessClass_givenInvalidTrainerId() {

        // given
        Long trainerId = 0L;
        Long fitnessClassId = 1L;

        FitnessClass fitnessClass = FitnessClass.builder()
                .id(fitnessClassId)
                .name("name")
                .difficultyLevel(DifficultyLevel.BEGINNER)
                .description("description")
                .trainers(new ArrayList<>())
                .build();

        given(fitnessClassRepository.findById(fitnessClassId)).willReturn(Optional.of(fitnessClass));
        given(trainerRepository.findById(trainerId)).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> fitnessClassService.assignTrainer(fitnessClassId, trainerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("Trainer with id: %s not found", trainerId));
    }

    @Test
    void shouldUnassignTrainerToFitnessClass_givenValidTrainerIdAndFitnessClassId() {

        // given
        Long trainerId = 1L;
        Long fitnessClassId = 1L;

        Trainer trainer = Trainer.builder()
                .id(trainerId)
                .firstName("first_name")
                .lastName("last_name")
                .email("email@email.com")
                .description("description")
                .build();

        List<Trainer> trainers = new ArrayList<>();
        trainers.add(trainer);

        FitnessClass fitnessClass = FitnessClass.builder()
                .id(fitnessClassId)
                .name("name")
                .difficultyLevel(DifficultyLevel.BEGINNER)
                .description("description")
                .trainers(trainers)
                .build();

        given(fitnessClassRepository.findById(fitnessClassId)).willReturn(Optional.of(fitnessClass));
        given(trainerRepository.findById(trainerId)).willReturn(Optional.of(trainer));

        // when
        boolean result = fitnessClassService.unassignTrainer(fitnessClassId, trainerId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void shouldNotUnassignTrainerToFitnessClass_whenTrainerIsNotAssignedToFitnessClass() {

        // given
        Long trainerId = 1L;
        Long fitnessClassId = 1L;

        Trainer trainer = Trainer.builder()
                .id(trainerId)
                .firstName("first_name")
                .lastName("last_name")
                .email("email@email.com")
                .description("description")
                .build();

        Trainer subscribedTrainer = Trainer.builder()
                .id(2L)
                .firstName("first_name")
                .lastName("last_name")
                .email("email@email.com")
                .description("description")
                .build();

        FitnessClass fitnessClass = FitnessClass.builder()
                .id(fitnessClassId)
                .name("name")
                .difficultyLevel(DifficultyLevel.BEGINNER)
                .description("description")
                .trainers(List.of(subscribedTrainer))
                .build();

        given(fitnessClassRepository.findById(fitnessClassId)).willReturn(Optional.of(fitnessClass));
        given(trainerRepository.findById(trainerId)).willReturn(Optional.of(trainer));

        // when then
        assertThatThrownBy(() -> fitnessClassService.unassignTrainer(fitnessClassId, trainerId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format(
                        "Trainer with id: %s is not assigned to the fitness class with id: %s", trainerId, fitnessClassId));
    }

    @Test
    void shouldNotUnassignTrainerToFitnessClass_givenInvalidFitnessClassId() {

        // given
        Long trainerId = 1L;
        Long fitnessClassId = 0L;

        given(fitnessClassRepository.findById(fitnessClassId)).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> fitnessClassService.unassignTrainer(fitnessClassId, trainerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("Fitness class with id: %s not found", fitnessClassId));
    }

    @Test
    void shouldNotUnassignTrainerToFitnessClass_givenInvalidTrainerId() {

        // given
        Long trainerId = 0L;
        Long fitnessClassId = 1L;

        FitnessClass fitnessClass = FitnessClass.builder()
                .id(fitnessClassId)
                .name("name")
                .difficultyLevel(DifficultyLevel.BEGINNER)
                .description("description")
                .trainers(new ArrayList<>())
                .build();

        given(fitnessClassRepository.findById(fitnessClassId)).willReturn(Optional.of(fitnessClass));
        given(trainerRepository.findById(trainerId)).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> fitnessClassService.unassignTrainer(fitnessClassId, trainerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("Trainer with id: %s not found", trainerId));
    }
}