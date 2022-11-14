package com.junior.company.fitness_studio_management.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "gym_event")
@SuperBuilder
@Getter
@NoArgsConstructor
public class GymEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "duration")
    @Setter
    private String duration;

    @Column(name = "participants_limit")
    private int participantsLimit;

    @Column(name = "current_participants_number")
    private int currentParticipantsNumber = 0;

    @ManyToMany
    @JoinTable(name = "event_user",
            joinColumns = @JoinColumn(name = "gym_event_id"),
            inverseJoinColumns = @JoinColumn(name = "app_user_id"))
    private List<AppUser> enrolledParticipants;

    @ManyToOne
    @JoinColumn(name = "fitness_class_id")
    @Setter
    private FitnessClass fitnessClass;
    
    public boolean addParticipant(AppUser appUser) {
        if (Objects.equals(enrolledParticipants, null)) {
            enrolledParticipants = new ArrayList<>();
        }

        if (participantsLimit > 0 &&
                participantsLimit > currentParticipantsNumber &&
                startTime.isAfter(LocalDateTime.now()) &&
                appUser != null) {

            for (AppUser user : enrolledParticipants) {
                if (Objects.equals(user, appUser)) {
                    return false;
                }
            }
            currentParticipantsNumber++;
            enrolledParticipants.add(appUser);
            return true;
        }
        return false;
    }

    public boolean removeParticipant(AppUser appUser) {
        if (Objects.equals(enrolledParticipants, null)) {
            enrolledParticipants = new ArrayList<>();
        }

        if (startTime.isAfter(LocalDateTime.now()) &&
                appUser != null) {
            for (AppUser user : enrolledParticipants) {
                if (Objects.equals(user, appUser)) {
                    currentParticipantsNumber--;
                    enrolledParticipants.remove(appUser);
                    return true;
                }
            }
        }
        return false;
    }
}
