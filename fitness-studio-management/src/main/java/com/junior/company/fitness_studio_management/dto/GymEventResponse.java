package com.junior.company.fitness_studio_management.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.junior.company.fitness_studio_management.model.FitnessClass;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@SuperBuilder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GymEventResponse {

    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    private String duration;
    private int participantsLimit;
    private int currentParticipantsNumber;
    private List<AppUserResponse> enrolledParticipants;
    private FitnessClass fitnessClass;

}
