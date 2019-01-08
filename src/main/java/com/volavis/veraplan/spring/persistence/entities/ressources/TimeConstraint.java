package com.volavis.veraplan.spring.persistence.entities.ressources;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "timeconstraints")
public class TimeConstraint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "timeconstraint_timeslots",
            joinColumns = @JoinColumn(name = "timeconstraint_id"),
            inverseJoinColumns = @JoinColumn(name = "timeslot_id"))
    private List<TimeSlot> timeSlots = new ArrayList<>();

    @NotBlank
    @Size(max = 40)
    private String name;

    @Size(max = 256)
    private String description;

    public TimeConstraint() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }
}
