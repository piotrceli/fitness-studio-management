package com.junior.company.fitness_studio_management.controller;

import com.junior.company.fitness_studio_management.dto.GymEventRequest;
import com.junior.company.fitness_studio_management.model.Response;
import com.junior.company.fitness_studio_management.service.GymEventService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

import static com.junior.company.fitness_studio_management.swagger.SwaggerConstants.GYM_EVENTS_API_TAG;

@RestController
@RequestMapping("api/v1/gym-events")
@RequiredArgsConstructor
@Api(tags = {GYM_EVENTS_API_TAG})
public class GymEventController {

    private final GymEventService gymEventService;

    @GetMapping
    @ApiOperation(value = "Get a list of all gym events", notes = "Available for EVERYONE\n\n" +
            "Allows to view a list of all gym events registered in the system sorted by their start time.")
    public ResponseEntity<Response> findAllGymEvents() {
        return ResponseEntity.ok(Response.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Retrieved list of gym events")
                .data(Map.of("gym_events", gymEventService.findAllGymEvents()))
                .build());
    }

    @GetMapping("{gymEventId}")
    @ApiOperation(value = "Get a gym event by id", notes = "Available for EVERYONE\n\n" +
            "Allows to view a gym event by id.")
    public ResponseEntity<Response> findGymEventById(@PathVariable Long gymEventId) {
        return ResponseEntity.ok(Response.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(String.format("Retrieved gym event by id: %s", gymEventId))
                .data(Map.of("gym_event", gymEventService.findGymEventById(gymEventId)))
                .build());
    }

    @GetMapping("mng/{gymEventId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Get a gym event with participants by id", notes = "Available for ADMIN\n\n" +
            "Allows to view a gym event by id together with its participants (users).")
    public ResponseEntity<Response> findGymEventByIdWithParticipants(@PathVariable Long gymEventId) {
        return ResponseEntity.ok(Response.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(String.format("Retrieved gym event by id: %s with participants", gymEventId))
                .data(Map.of("gym_event", gymEventService.findGymEventByIdWithParticipants(gymEventId)))
                .build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Create a new gym event", notes = "Available for ADMIN\n\n" +
            "Allows to register a new gym event in the system. The gym event must be dated correctly.")
    public ResponseEntity<Response> createGymEvent(@Valid @RequestBody GymEventRequest gymEventRequest) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/gym-events").toUriString());
        return ResponseEntity.created(uri).body(Response.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CREATED)
                .statusCode(HttpStatus.CREATED.value())
                .message("Created new gym event")
                .data(Map.of("gym_event", gymEventService.createGymEvent(gymEventRequest)))
                .build());
    }

    @DeleteMapping("{gymEventId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Delete an existing gym event by id", notes = "Available for ADMIN\n\n" +
            "Allows to delete an existing gym event by id from the system.")
    public ResponseEntity<Response> deleteGymEventById(@PathVariable Long gymEventId) {
        return ResponseEntity.ok(Response.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(String.format("Deleted gym event with id: %s", gymEventId))
                .data(Map.of("is_deleted", gymEventService.deleteGymEventById(gymEventId)))
                .build());
    }

    @PostMapping("enroll/{gymEventId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "Enroll an user in a gym event", notes = "Available for USER\n\n" +
            "Allows the current logged user to enroll in a gym event if he is not enrolled yet." +
            "The user can enroll in the gym event that has not started and which the limit of participants allows it.")
    public ResponseEntity<Response> enrollInEvent(@PathVariable Long gymEventId) {
        return ResponseEntity.ok(Response.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Enrolled in event")
                .data(Map.of("is_enrolled", gymEventService.enrollUser(gymEventId)))
                .build());
    }

    @PostMapping("disenroll/{gymEventId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "Disenroll an user from a gym event", notes = "Available for USER\n\n" +
            "Allows the current logged user to disenroll from a gym event if he is already enrolled." +
            "The gym event cannot be started already.")
    public ResponseEntity<Response> disenrollFromEvent(@PathVariable Long gymEventId) {
        return ResponseEntity.ok(Response.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Disenrolled from event")
                .data(Map.of("is_disenrolled", gymEventService.disenrollUser(gymEventId)))
                .build());
    }
}
