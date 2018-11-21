package com.volavis.veraplan.spring.persistence.entities.organisation;

import com.volavis.veraplan.spring.persistence.entities.ressources.TimeSlot;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "event_room",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id"))
    private Set<TimeSlot> rooms = new HashSet<>();


    @NotBlank
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "event_timeslot",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "timeslot_id"))
    private Set<TimeSlot> timeSlots = new HashSet<>();
}
