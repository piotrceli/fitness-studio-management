package com.junior.company.fitness_studio_management.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "app_user")
@Getter
@SuperBuilder
@NoArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "date_of_birth")
    private LocalDate dob;

    @Column(name = "enabled")
    private boolean isEnabled;

    @ManyToMany
    @JoinTable(name = "event_user",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "gym_event_id"))
    private List<GymEvent> gymEvents;

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
