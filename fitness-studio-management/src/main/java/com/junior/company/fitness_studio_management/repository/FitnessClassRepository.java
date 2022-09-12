package com.junior.company.fitness_studio_management.repository;

import com.junior.company.fitness_studio_management.model.FitnessClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FitnessClassRepository extends JpaRepository<FitnessClass, Long> {
}
