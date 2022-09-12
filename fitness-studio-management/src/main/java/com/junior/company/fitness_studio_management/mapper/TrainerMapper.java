package com.junior.company.fitness_studio_management.mapper;

import com.junior.company.fitness_studio_management.dto.TrainerRequest;
import com.junior.company.fitness_studio_management.model.Trainer;

public class TrainerMapper {

    private static final Long EMPTY_ID = null;

    public static Trainer mapTrainerRequestToTrainerCreate(TrainerRequest trainerRequest) {

        return Trainer.builder()
                .id(EMPTY_ID)
                .firstName(trainerRequest.getFirstName())
                .lastName(trainerRequest.getLastName())
                .email(trainerRequest.getEmail())
                .description(trainerRequest.getDescription())
                .build();
    }

    public static Trainer mapTrainerRequestToTrainerUpdate(TrainerRequest trainerRequest) {

        return Trainer.builder()
                .id(trainerRequest.getId())
                .firstName(trainerRequest.getFirstName())
                .lastName(trainerRequest.getLastName())
                .email(trainerRequest.getEmail())
                .description(trainerRequest.getDescription())
                .build();
    }


}
