package com.junior.company.fitness_studio_management.service;

import com.junior.company.fitness_studio_management.dto.TrainerRequest;
import com.junior.company.fitness_studio_management.exception.ResourceNotFoundException;
import com.junior.company.fitness_studio_management.mapper.TrainerMapper;
import com.junior.company.fitness_studio_management.model.Trainer;
import com.junior.company.fitness_studio_management.repository.TrainerRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
class TrainerServiceImplTest {

    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainerServiceImpl trainerService;


    @Test
    void shouldGetListOfTrainers() {

        // when
        trainerService.findAllTrainers();

        // then
        verify(trainerRepository, times(1)).findAll();
    }

    @Test
    void shouldFindTrainerById_givenValidTrainerId() {

        // given
        Long trainerId = 1L;
        Trainer trainer = Trainer.builder()
                .id(trainerId)
                .firstName("first_name")
                .lastName("last_name")
                .email("email@email.com")
                .description("description")
                .build();

        given(trainerRepository.findById(trainerId)).willReturn(Optional.of(trainer));

        // when
        Trainer result = trainerService.findTrainerById(trainerId);

        // then
        assertThat(result).isEqualTo(trainer);
    }

    @Test
    void shouldNotFindTrainerId_givenInvalidTrainerId() {

        // given
        Long trainerId = 0L;

        // when then
        assertThatThrownBy(() -> trainerService.findTrainerById(trainerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("Trainer with id: %s not found", trainerId));
    }

    @Test
    void shouldCreateTrainer_givenValidTrainerRequest() {

        // given
        TrainerRequest trainerRequest = TrainerRequest.builder()
                .id(1L)
                .firstName("first_name")
                .lastName("last_name")
                .email("email@email.com")
                .description("description")
                .build();

        Trainer trainer = TrainerMapper.mapTrainerRequestToTrainerCreate(trainerRequest);

        given(trainerRepository.save(any())).willReturn(trainer);

        // when
        Trainer result = trainerService.createTrainer(trainerRequest);

        // then
        ArgumentCaptor<Trainer> trainerArgumentCaptor = ArgumentCaptor.forClass(Trainer.class);
        verify(trainerRepository).save(trainerArgumentCaptor.capture());
        Trainer capturedTrainer = trainerArgumentCaptor.getValue();

        assertThat(capturedTrainer).usingRecursiveComparison().isEqualTo(trainer);
        assertThat(result).isEqualTo(trainer);
    }

    @Test
    void shouldUpdateTrainer_givenValidTrainerRequest() {

        // given
        TrainerRequest trainerRequest = TrainerRequest.builder()
                .id(1L)
                .firstName("first_name")
                .lastName("last_name")
                .email("email@email.com")
                .description("description")
                .build();

        Trainer trainer = TrainerMapper.mapTrainerRequestToTrainerUpdate(trainerRequest);


        given(trainerRepository.findById(anyLong())).willReturn(Optional.of(trainer));
        given(trainerRepository.save(any())).willReturn(trainer);


        // when
        Trainer result = trainerService.updateTrainer(trainerRequest);

        // then
        ArgumentCaptor<Trainer> trainerArgumentCaptor = ArgumentCaptor.forClass(Trainer.class);
        verify(trainerRepository).save(trainerArgumentCaptor.capture());
        Trainer capturedTrainer = trainerArgumentCaptor.getValue();

        assertThat(capturedTrainer).usingRecursiveComparison().isEqualTo(trainer);
        assertThat(result).isEqualTo(trainer);
    }

    @Test
    void shouldNotUpdateTrainer_givenInvalidTrainerRequest() {

        // given
        TrainerRequest trainerRequest = TrainerRequest.builder()
                .id(1L)
                .firstName("first_name")
                .lastName("last_name")
                .email("email@email.com")
                .description("description")
                .build();

        given(trainerRepository.findById(anyLong())).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> trainerService.updateTrainer(trainerRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("Trainer with id: %s not found", trainerRequest.getId()));
    }

    @Test
    void shouldDeleteTrainerById_givenValidTrainerId() {

        // given
        Long trainerId = 1L;
        Trainer trainer = Trainer.builder()
                .id(trainerId)
                .firstName("first_name")
                .lastName("last_name")
                .email("email@email.com")
                .description("description")
                .build();

        given(trainerRepository.findById(anyLong())).willReturn(Optional.of(trainer));

        // when
        boolean result = trainerService.deleteTrainerById(trainerId);

        // then
        assertThat(result).isTrue();
        verify(trainerRepository, times(1)).delete(trainer);

    }

    @Test
    void shouldNotDeleteTrainerById_givenInvalidTrainerId() {

        // given
        Long trainerId = 0L;

        given(trainerRepository.findById(anyLong())).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> trainerService.deleteTrainerById(trainerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("Trainer with id: %s not found", trainerId));
    }
}