package com.junior.company.fitness_studio_management.mapper;

import com.junior.company.fitness_studio_management.dto.FitnessClassRequest;
import com.junior.company.fitness_studio_management.model.DifficultyLevel;
import com.junior.company.fitness_studio_management.model.FitnessClass;
import com.junior.company.fitness_studio_management.model.Trainer;

import java.util.List;

public class FitnessClassMapper {

    private static final Long EMPTY_ID = null;

    public static FitnessClass mapFitnessClassRequestToFitnessClassCreate(FitnessClassRequest fitnessClassRequest) {

        return FitnessClass.builder()
                .id(EMPTY_ID)
                .name(fitnessClassRequest.getName())
                .difficultyLevel(DifficultyLevel.valueOf(fitnessClassRequest.getDifficultyLevel()))
                .description(fitnessClassRequest.getDescription())
                .build();
    }

    public static FitnessClass mapFitnessClassRequestToFitnessClassUpdate(FitnessClassRequest fitnessClassRequest, List<Trainer> trainers) {

        return FitnessClass.builder()
                .id(fitnessClassRequest.getId())
                .name(fitnessClassRequest.getName())
                .difficultyLevel(DifficultyLevel.valueOf(fitnessClassRequest.getDifficultyLevel()))
                .description(fitnessClassRequest.getDescription())
                .trainers(trainers)
                .build();
    }
}
