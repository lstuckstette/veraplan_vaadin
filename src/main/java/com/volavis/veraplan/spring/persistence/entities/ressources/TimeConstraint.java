package com.volavis.veraplan.spring.persistence.entities.ressources;

import javax.persistence.*;
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

    public TimeConstraint() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }
}
