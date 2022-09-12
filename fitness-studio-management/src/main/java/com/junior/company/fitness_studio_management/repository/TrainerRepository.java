package com.junior.company.fitness_studio_management.repository;

import com.junior.company.fitness_studio_management.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
}
