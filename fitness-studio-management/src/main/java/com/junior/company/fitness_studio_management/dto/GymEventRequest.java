package com.junior.company.fitness_studio_management.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
public class GymEventRequest {

    @NotNull(message = "Cannot be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @ApiModelProperty(notes = "Start time of an event (yyyy-MM-dd HH:mm)", example = "2022-12-01 12:00")
    private LocalDateTime startTime;

    @NotNull(message = "Cannot be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @ApiModelProperty(notes = "End time of an event (yyyy-MM-dd HH:mm)", example = "2022-12-01 13:30")
    private LocalDateTime endTime;

    @Min(value = 1, message = "Must be min 1")
    @NotNull(message = "Cannot be empty")
    @ApiModelProperty(notes = "Limit of participants of an event", example = "20")
    private int participantsLimit;

    @NotNull(message = "Cannot be empty")
    @ApiModelProperty(notes = "Id of fitness class involved in the event ", example = "5")
    private Long fitnessClassId;
}
