package com.junior.company.fitness_studio_management.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "fitness_class")
@SuperBuilder
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FitnessClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fitness_class_name")
    private String name;

    @Column(name = "difficulty_level")
    private DifficultyLevel difficultyLevel;

    @Column(name = "fitness_class_description")
    private String description;

    @ManyToMany
    @JoinTable(name = "fitness_class_trainer",
            joinColumns = @JoinColumn(name = "fitness_class_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id"))
    private List<Trainer> trainers;


    public boolean addTrainer(Trainer trainer) {
        return trainers.add(trainer);
    }

    public boolean removeTrainer(Trainer trainer) {
        return trainers.remove(trainer);
    }
}
