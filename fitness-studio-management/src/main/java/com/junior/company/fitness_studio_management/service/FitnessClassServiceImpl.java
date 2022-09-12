package com.junior.company.fitness_studio_management.service;

import com.junior.company.fitness_studio_management.dto.FitnessClassRequest;
import com.junior.company.fitness_studio_management.exception.ResourceNotFoundException;
import com.junior.company.fitness_studio_management.mapper.FitnessClassMapper;
import com.junior.company.fitness_studio_management.model.FitnessClass;
import com.junior.company.fitness_studio_management.model.Trainer;
import com.junior.company.fitness_studio_management.repository.FitnessClassRepository;
import com.junior.company.fitness_studio_management.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FitnessClassServiceImpl implements FitnessClassService {

    private final FitnessClassRepository fitnessClassRepository;
    private final TrainerRepository trainerRepository;

    @Override
    public List<FitnessClass> findAllFitnessClasses() {
        log.info("Getting list of all fitness classes");
        return fitnessClassRepository.findAll();
    }

    @Override
    public FitnessClass findFitnessClassById(Long fitnessClassId) {
        log.info("Getting fitness class by id: {}", fitnessClassId);
        return fitnessClassRepository.findById(fitnessClassId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Fitness class with id: %s not found", fitnessClassId)));
    }

    @Override
    public FitnessClass createFitnessClass(FitnessClassRequest fitnessClassRequest) {
        log.info("Creating fitness class");
        FitnessClass fitnessClass = FitnessClassMapper.mapFitnessClassRequestToFitnessClassCreate(fitnessClassRequest);
        return fitnessClassRepository.save(fitnessClass);
    }

    @Override
    public FitnessClass updateFitnessClass(FitnessClassRequest fitnessClassRequest) {
        log.info("Updating fitness class with id: {}", fitnessClassRequest.getId());
        FitnessClass foundFitnessClass = fitnessClassRepository.findById(fitnessClassRequest.getId()).orElseThrow(() ->
                new ResourceNotFoundException(
                        String.format("Fitness class with id: %s not found", fitnessClassRequest.getId())));

        FitnessClass fitnessClass = FitnessClassMapper
                .mapFitnessClassRequestToFitnessClassUpdate(fitnessClassRequest, foundFitnessClass.getTrainers());
        return fitnessClassRepository.save(fitnessClass);
    }

    @Override
    public boolean deleteFitnessClassById(Long fitnessClassId) {
        log.info("Deleting fitness class with id: {}", fitnessClassId);
        FitnessClass foundFitnessClass = fitnessClassRepository.findById(fitnessClassId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Fitness class with id: %s not found", fitnessClassId)));

        fitnessClassRepository.delete(foundFitnessClass);
        return true;
    }


    @Override
    public boolean assignTrainer(Long fitnessClassId, Long trainerId) {
        log.info("Assigning trainer with id: {} to fitness class with id: {}", trainerId, fitnessClassId);
        FitnessClass foundFitnessClass = fitnessClassRepository.findById(fitnessClassId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Fitness class with id: %s not found", fitnessClassId)));
        Trainer foundTrainer = trainerRepository.findById(trainerId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Trainer with id: %s not found", trainerId)));

        foundFitnessClass.getTrainers().forEach((trainer) -> {
            if (Objects.equals(trainerId, trainer.getId())) {
                throw new IllegalStateException(String.format(
                        "Trainer with id: %s is already assigned to the fitness class with id: %s", trainerId, fitnessClassId));
            }
        });
        return foundFitnessClass.addTrainer(foundTrainer);
    }

    @Override
    public boolean unassignTrainer(Long fitnessClassId, Long trainerId) {
        log.info("Unassigning trainer with id: {} from fitness class with id: {}", trainerId, fitnessClassId);
        FitnessClass foundFitnessClass = fitnessClassRepository.findById(fitnessClassId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Fitness class with id: %s not found", fitnessClassId)));
        Trainer foundTrainer = trainerRepository.findById(trainerId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Trainer with id: %s not found", trainerId)));


        for (Trainer trainer : foundFitnessClass.getTrainers()) {
            if (Objects.equals(trainerId, trainer.getId())) {
                return foundFitnessClass.removeTrainer(foundTrainer);
            }
        }
        throw new IllegalStateException(String.format(
                "Trainer with id: %s is not assigned to the fitness class with id: %s", trainerId, fitnessClassId));
    }
}
