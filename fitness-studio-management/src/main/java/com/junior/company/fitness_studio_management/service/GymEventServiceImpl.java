package com.junior.company.fitness_studio_management.service;

import com.junior.company.fitness_studio_management.dto.GymEventRequest;
import com.junior.company.fitness_studio_management.dto.GymEventResponse;
import com.junior.company.fitness_studio_management.exception.InvalidGymEventDateException;
import com.junior.company.fitness_studio_management.exception.ResourceNotFoundException;
import com.junior.company.fitness_studio_management.mapper.GymEventMapper;
import com.junior.company.fitness_studio_management.model.AppUser;
import com.junior.company.fitness_studio_management.model.FitnessClass;
import com.junior.company.fitness_studio_management.model.GymEvent;
import com.junior.company.fitness_studio_management.repository.FitnessClassRepository;
import com.junior.company.fitness_studio_management.repository.GymEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GymEventServiceImpl implements GymEventService {

    private final GymEventRepository gymEventRepository;
    private final FitnessClassRepository fitnessClassRepository;
    private final AppUserService appUserService;


    @Override
    public List<GymEventResponse> findAllGymEvents() {
        log.info("Retrieving list of gym events");
        List<GymEvent> gymEvents = gymEventRepository.findAll(Sort.by("startTime"));
        return GymEventMapper.mapGymEventListToGymEventResponseList(gymEvents);
    }

    @Override
    public GymEventResponse findGymEventById(Long gymEventId) {
        log.info("Getting gym event by id: {}", gymEventId);
        GymEvent gymEvent = gymEventRepository.findById(gymEventId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Gym event with id: %s not found", gymEventId)));
        return GymEventMapper.mapGymEventToGymEventResponse(gymEvent);
    }

    @Override
    public GymEventResponse findGymEventByIdWithParticipants(Long gymEventId) {
        log.info("Getting gym event by id with participants: {}", gymEventId);
        GymEvent gymEvent = gymEventRepository.findById(gymEventId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Gym event with id: %s not found", gymEventId)));
        return GymEventMapper.mapGymEventToGymEventResponseMng(gymEvent);
    }

    @Override
    public GymEvent createGymEvent(GymEventRequest gymEventRequest) {
        log.info("Creating gym event");

        if (gymEventRequest.getStartTime().isBefore(LocalDateTime.now()) ||
                gymEventRequest.getStartTime().isAfter(gymEventRequest.getEndTime()) ||
                gymEventRequest.getStartTime().isEqual(gymEventRequest.getEndTime())) {
            throw new InvalidGymEventDateException("Entered dates are not valid");
        }

        FitnessClass foundFitnessClass = fitnessClassRepository.findById(gymEventRequest.getFitnessClassId()).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Fitness class with id: %s not found", gymEventRequest.getFitnessClassId())));

        GymEvent gymEvent = GymEventMapper.mapGymEventRequestToGymEvent(gymEventRequest);
        gymEvent.setDuration(calculateGymEventDuration(gymEvent));
        gymEvent.setFitnessClass(foundFitnessClass);
        return gymEventRepository.save(gymEvent);
    }

    private String calculateGymEventDuration(GymEvent gymEvent) {
        Duration duration = Duration.between(gymEvent.getStartTime(), gymEvent.getEndTime());
        int hours = (int) duration.toHours();
        int minutes = (int) duration.toMinutes() % 60;
        LocalTime time = LocalTime.of(hours, minutes);
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    @Override
    public boolean deleteGymEventById(Long gymEventId) {
        log.info("Deleting gym event with id: {}", gymEventId);
        GymEvent foundGymEvent = gymEventRepository.findById(gymEventId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Gym event with id: %s not found", gymEventId)));

        gymEventRepository.delete(foundGymEvent);
        return true;
    }

    @Override
    public boolean enrollUser(Long gymEventId) {
        log.info("Enrolling user in gym event with id: {}", gymEventId);
        GymEvent foundGymEvent = gymEventRepository.findById(gymEventId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Gym event with id: %s not found", gymEventId)));

        AppUser currentUser = appUserService.getCurrentUser();
        return foundGymEvent.addParticipant(currentUser);
    }

    @Override
    public boolean disenrollUser(Long gymEventId) {
        log.info("Disenrolling user from gym event with id: {}", gymEventId);
        GymEvent foundGymEvent = gymEventRepository.findById(gymEventId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Gym event with id: %s not found", gymEventId)));

        AppUser currentUser = appUserService.getCurrentUser();
        return foundGymEvent.removeParticipant(currentUser);
    }
}
