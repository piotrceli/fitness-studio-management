package com.junior.company.fitness_studio_management.mapper;

import com.junior.company.fitness_studio_management.dto.GymEventRequest;
import com.junior.company.fitness_studio_management.dto.GymEventResponse;
import com.junior.company.fitness_studio_management.model.GymEvent;

import java.util.List;
import java.util.stream.Collectors;

public class GymEventMapper {

    private static final Long EMPTY_ID = null;

    public static GymEvent mapGymEventRequestToGymEvent(GymEventRequest gymEventRequest) {

        return GymEvent.builder()
                .id(EMPTY_ID)
                .startTime(gymEventRequest.getStartTime())
                .endTime(gymEventRequest.getEndTime())
                .participantsLimit(gymEventRequest.getParticipantsLimit())
                .build();
    }

    public static List<GymEventResponse> mapGymEventListToGymEventResponseList(List<GymEvent> gymEvents) {

        return gymEvents.stream().map((gymEvent) ->
                        GymEventResponse.builder()
                                .startTime(gymEvent.getStartTime())
                                .endTime(gymEvent.getEndTime())
                                .duration(gymEvent.getDuration())
                                .participantsLimit(gymEvent.getParticipantsLimit())
                                .currentParticipantsNumber(gymEvent.getCurrentParticipantsNumber())
                                .fitnessClass(gymEvent.getFitnessClass())
                                .build())
                .collect(Collectors.toList());
    }

    public static GymEventResponse mapGymEventToGymEventResponse(GymEvent gymEvent) {

        return GymEventResponse.builder()
                .startTime(gymEvent.getStartTime())
                .endTime(gymEvent.getEndTime())
                .duration(gymEvent.getDuration())
                .participantsLimit(gymEvent.getParticipantsLimit())
                .currentParticipantsNumber(gymEvent.getCurrentParticipantsNumber())
                .fitnessClass(gymEvent.getFitnessClass())
                .build();
    }

    public static GymEventResponse mapGymEventToGymEventResponseMng(GymEvent gymEvent) {

        return GymEventResponse.builder()
                .id(gymEvent.getId())
                .startTime(gymEvent.getStartTime())
                .endTime(gymEvent.getEndTime())
                .duration(gymEvent.getDuration())
                .participantsLimit(gymEvent.getParticipantsLimit())
                .currentParticipantsNumber(gymEvent.getCurrentParticipantsNumber())
                .enrolledParticipants(AppUserMapper.mapAppUserListToAppUserResponseList(gymEvent.getEnrolledParticipants()))
                .fitnessClass(gymEvent.getFitnessClass())
                .build();
    }


}
