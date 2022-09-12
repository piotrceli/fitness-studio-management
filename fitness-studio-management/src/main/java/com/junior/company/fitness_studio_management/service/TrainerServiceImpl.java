package com.junior.company.fitness_studio_management.service;

import com.junior.company.fitness_studio_management.dto.TrainerRequest;
import com.junior.company.fitness_studio_management.exception.ResourceNotFoundException;
import com.junior.company.fitness_studio_management.mapper.TrainerMapper;
import com.junior.company.fitness_studio_management.model.Trainer;
import com.junior.company.fitness_studio_management.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;

    @Override
    public List<Trainer> findAllTrainers() {
        log.info("Getting list of all trainers");
        return trainerRepository.findAll();
    }

    @Override
    public Trainer findTrainerById(Long trainerId) {
        log.info("Getting trainer by id: {}", trainerId);
        return trainerRepository.findById(trainerId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Trainer with id: %s not found", trainerId)));
    }

    @Override
    public Trainer createTrainer(TrainerRequest trainerRequest) {
        log.info("Creating trainer");
        Trainer trainer = TrainerMapper.mapTrainerRequestToTrainerCreate(trainerRequest);
        return trainerRepository.save(trainer);
    }

    @Override
    public Trainer updateTrainer(TrainerRequest trainerRequest) {
        log.info("Updating trainer with id: {}", trainerRequest.getId());
        if (trainerRepository.findById(trainerRequest.getId()).isEmpty()){
            throw new ResourceNotFoundException(
                    String.format("Trainer with id: %s not found", trainerRequest.getId()));
        }
        Trainer trainer = TrainerMapper.mapTrainerRequestToTrainerUpdate(trainerRequest);
        return trainerRepository.save(trainer);
    }


    @Override
    public boolean deleteTrainerById(Long trainerId) {
        log.info("Deleting trainer with id: {}", trainerId);
        Trainer trainer = trainerRepository.findById(trainerId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Trainer with id: %s not found", trainerId)));

        trainerRepository.delete(trainer);
        return true;
    }
}
