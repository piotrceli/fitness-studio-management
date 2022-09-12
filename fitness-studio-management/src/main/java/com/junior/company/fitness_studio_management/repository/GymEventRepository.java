package com.junior.company.fitness_studio_management.repository;

import com.junior.company.fitness_studio_management.model.GymEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GymEventRepository extends JpaRepository<GymEvent, Long> {
}
