package com.junior.company.fitness_studio_management.service;

import com.junior.company.fitness_studio_management.dto.FitnessClassRequest;
import com.junior.company.fitness_studio_management.model.FitnessClass;

import java.util.List;

public interface FitnessClassService {

    List<FitnessClass> findAllFitnessClasses();

    FitnessClass findFitnessClassById(Long fitnessClassId);

    FitnessClass createFitnessClass(FitnessClassRequest fitnessClassRequest);

    FitnessClass updateFitnessClass(FitnessClassRequest fitnessClassRequest);

    boolean deleteFitnessClassById(Long fitnessClassId);

    boolean assignTrainer(Long fitnessClassId, Long trainerId);

    boolean unassignTrainer(Long fitnessClassId, Long trainerId);
}
