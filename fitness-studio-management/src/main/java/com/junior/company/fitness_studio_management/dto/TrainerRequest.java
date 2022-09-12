package com.junior.company.fitness_studio_management.dto;

import com.junior.company.fitness_studio_management.validation.ValidEmail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@SuperBuilder
@NoArgsConstructor
public class TrainerRequest {

    private Long id;

    @NotBlank(message = "Cannot be empty")
    @Length(min = 2, message = "Min length is 2")
    @ApiModelProperty(notes = "Trainer's first name", example = "Sam")
    private String firstName;

    @NotBlank(message = "Cannot be empty")
    @Length(min = 2, message = "Min length is 2")
    @ApiModelProperty(notes = "Trainer's last name", example = "Hug")
    private String lastName;

    @NotBlank(message = "Cannot be empty")
    @ValidEmail
    @ApiModelProperty(notes = "Trainer's email", example = "email@domain.com")
    private String email;

    @NotBlank(message = "Cannot be empty")
    @Length(min = 2, message = "Min length is 2")
    @ApiModelProperty(notes = "Trainer's description", example = "Yoga trainer with 5 years of experience")
    private String description;
}
