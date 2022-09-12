package com.junior.company.fitness_studio_management.service;

import com.junior.company.fitness_studio_management.dto.GymEventRequest;
import com.junior.company.fitness_studio_management.dto.GymEventResponse;
import com.junior.company.fitness_studio_management.model.GymEvent;

import java.util.List;

public interface GymEventService {

    List<GymEventResponse> findAllGymEvents();

    GymEventResponse findGymEventById(Long gymEventId);

    GymEventResponse findGymEventByIdWithParticipants(Long gymEventId);

    GymEvent createGymEvent(GymEventRequest gymEventRequest);

    boolean deleteGymEventById(Long gymEventId);

    boolean enrollUser(Long gymEventId);

    boolean disenrollUser(Long gymEventId);
}
