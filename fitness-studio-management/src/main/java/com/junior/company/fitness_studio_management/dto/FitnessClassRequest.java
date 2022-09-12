package com.junior.company.fitness_studio_management.dto;

import com.junior.company.fitness_studio_management.validation.ValidDifficultyLevel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@SuperBuilder
@NoArgsConstructor
public class FitnessClassRequest {

    private Long id;

    @NotBlank(message = "Cannot be empty")
    @Length(min = 2, message = "Min length is 2")
    @ApiModelProperty(notes = "Name of a fitness class", example = "YOGA")
    private String name;

    @NotBlank(message = "Cannot be empty")
    @ValidDifficultyLevel
    @ApiModelProperty(notes = "Valid difficulty level (BEGINNER/INTERMEDIATE/ADVANCED)", example = "ADVANCED")
    private String difficultyLevel;

    @NotBlank(message = "Cannot be empty")
    @Length(min = 2, message = "Min length is 2")
    @ApiModelProperty(notes = "Description of a fitness class", example = "Yoga for everyone.")
    private String description;

}
