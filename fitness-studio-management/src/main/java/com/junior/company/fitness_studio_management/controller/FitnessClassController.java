package com.junior.company.fitness_studio_management.controller;

import com.junior.company.fitness_studio_management.dto.FitnessClassRequest;
import com.junior.company.fitness_studio_management.model.Response;
import com.junior.company.fitness_studio_management.service.FitnessClassService;
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

import static com.junior.company.fitness_studio_management.swagger.SwaggerConstants.FITNESS_CLASSES_API_TAG;

@RestController
@RequestMapping("api/v1/fitness-classes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Api(tags = {FITNESS_CLASSES_API_TAG})
public class FitnessClassController {

    private final FitnessClassService fitnessClassService;

    @GetMapping
    @ApiOperation(value = "Get a list of all fitness classes", notes = "Available for ADMIN\n\n" +
            "Allows to view a list of all fitness classes registered in the system.")
    public ResponseEntity<Response> findAllFitnessClasses() {
        return ResponseEntity.ok(Response.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Retrieved list of fitness classes")
                .data(Map.of("fitness_classes", fitnessClassService.findAllFitnessClasses()))
                .build());
    }

    @GetMapping("{fitnessClassId}")
    @ApiOperation(value = "Get a fitness class by id", notes = "Available for ADMIN\n\n" +
            "Allows to view a fitness class by id.")
    public ResponseEntity<Response> findFitnessClassById(@PathVariable Long fitnessClassId) {
        return ResponseEntity.ok(Response.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(String.format("Retrieved fitness class by id: %s", fitnessClassId))
                .data(Map.of("fitness_class", fitnessClassService.findFitnessClassById(fitnessClassId)))
                .build());
    }

    @PostMapping
    @ApiOperation(value = "Create a new fitness class", notes = "Available for ADMIN\n\n" +
            "Allows to register a new fitness class in the system.")
    public ResponseEntity<Response> createFitnessClass(@Valid @RequestBody FitnessClassRequest fitnessClassRequest) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/fitness-classes").toUriString());
        return ResponseEntity.created(uri).body(Response.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .message("Created new fitness class")
                .data(Map.of("fitness_class", fitnessClassService.createFitnessClass(fitnessClassRequest)))
                .build());
    }

    @PutMapping
    @ApiOperation(value = "Update an existing fitness class", notes = "Available for ADMIN\n\n" +
            "Allows to update an existing fitness class.")
    public ResponseEntity<Response> updateFitnessClass(@Valid @RequestBody FitnessClassRequest fitnessClassRequest) {
        return ResponseEntity.ok(Response.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Updated fitness class")
                .data(Map.of("fitness_class", fitnessClassService.updateFitnessClass(fitnessClassRequest)))
                .build());
    }

    @DeleteMapping("{fitnessClassId}")
    @ApiOperation(value = "Delete an existing fitness class by id", notes = "Available for ADMIN\n\n" +
            "Allows to delete an existing fitness class by id from the system.")
    public ResponseEntity<Response> deleteFitnessClassById(@PathVariable Long fitnessClassId) {
        return ResponseEntity.ok(Response.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(String.format("Deleted fitness class with id: %s", fitnessClassId))
                .data(Map.of("is_deleted", fitnessClassService.deleteFitnessClassById(fitnessClassId)))
                .build());
    }

    @PutMapping("assign/{fitnessClassId}/{trainerId}")
    @ApiOperation(value = "Assign a trainer to a fitness class", notes = "Available for ADMIN\n\n" +
            "Allows to assign a trainer to a fitness class if the trainer is not assigned yet.")
    public ResponseEntity<Response> assignTrainer(@PathVariable Long fitnessClassId,
                                                  @PathVariable Long trainerId) {
        return ResponseEntity.ok(Response.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(String.format("Assigned trainer with id: %s to fitness class with id: %s", trainerId, fitnessClassId))
                .data(Map.of("is_assigned", fitnessClassService.assignTrainer(fitnessClassId, trainerId)))
                .build());
    }

    @PutMapping("unassign/{fitnessClassId}/{trainerId}")
    @ApiOperation(value = "Unassign a trainer from a fitness class", notes = "Available for ADMIN\n\n" +
            "Allows to unassign a trainer from a fitness class if the trainer is already assigned.")
    public ResponseEntity<Response> unassignTrainer(@PathVariable Long fitnessClassId,
                                                    @PathVariable Long trainerId) {
        return ResponseEntity.ok(Response.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(String.format("Unassigned trainer with id: %s to fitness class with id: %s", trainerId, fitnessClassId))
                .data(Map.of("is_unassigned", fitnessClassService.unassignTrainer(fitnessClassId, trainerId)))
                .build());
    }

}
