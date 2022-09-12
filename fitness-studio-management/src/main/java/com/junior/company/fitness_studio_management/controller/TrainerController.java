package com.junior.company.fitness_studio_management.controller;

import com.junior.company.fitness_studio_management.dto.TrainerRequest;
import com.junior.company.fitness_studio_management.model.Response;
import com.junior.company.fitness_studio_management.service.TrainerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

import static com.junior.company.fitness_studio_management.swagger.SwaggerConstants.TRAINERS_API_TAG;

@RestController
@RequestMapping("api/v1/trainers")
@RequiredArgsConstructor
@Api(tags = {TRAINERS_API_TAG})
public class TrainerController {

    private final TrainerService trainerService;

    @GetMapping
    @ApiOperation(value = "Get a list of all trainers", notes = "Available for EVERYONE\n\n" +
            "Allows to view a list of all trainers registered in the system.")
    public ResponseEntity<Response> findAllTrainers() {
        return ResponseEntity.ok(Response.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Retrieved list of trainers")
                .data(Map.of("trainers", trainerService.findAllTrainers()))
                .build());
    }


    @GetMapping("{trainerId}")
    @ApiOperation(value = "Get a trainer by id", notes = "Available for EVERYONE\n\n" +
            "Allows to view a trainer by id.")
    public ResponseEntity<Response> findTrainerById(@PathVariable Long trainerId) {
        return ResponseEntity.ok(Response.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(String.format("Retrieved trainer by id: %s", trainerId))
                .data(Map.of("trainer", trainerService.findTrainerById(trainerId)))
                .build());
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Create a new trainer", notes = "Available for ADMIN\n\n" +
            "Allows to register a new trainer in the system.")
    public ResponseEntity<Response> createTrainer(@Valid @RequestBody TrainerRequest trainerRequest) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/trainers").toUriString());
        return ResponseEntity.created(uri).body(Response.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .message("Created new trainer")
                .data(Map.of("trainer", trainerService.createTrainer(trainerRequest)))
                .build());
    }


    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Update an existing trainer", notes = "Available for ADMIN\n\n" +
            "Allows to update an existing trainer.")
    public ResponseEntity<Response> updateTrainer(@Valid @RequestBody TrainerRequest trainerRequest) {
        return ResponseEntity.ok(Response.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Updated trainer")
                .data(Map.of("trainer", trainerService.updateTrainer(trainerRequest)))
                .build());
    }

    @DeleteMapping("{trainerId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Delete an existing trainer by id", notes = "Available for ADMIN\n\n" +
            "Allows to delete an existing trainer by id from the system.")
    public ResponseEntity<Response> deleteTrainerById(@PathVariable Long trainerId) {
        return ResponseEntity.ok(Response.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(String.format("Deleted trainer with id: %s", trainerId))
                .data(Map.of("is_deleted", trainerService.deleteTrainerById(trainerId)))
                .build());
    }
}
