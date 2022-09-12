package com.junior.company.fitness_studio_management.service;

import com.junior.company.fitness_studio_management.dto.TrainerRequest;
import com.junior.company.fitness_studio_management.model.Trainer;

import java.util.List;

public interface TrainerService {

    List<Trainer> findAllTrainers();

    Trainer findTrainerById(Long trainerId);

    Trainer createTrainer(TrainerRequest trainerRequest);

    Trainer updateTrainer(TrainerRequest trainerRequest);

    boolean deleteTrainerById(Long trainerId);
}
