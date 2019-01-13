package com.volavis.veraplan.spring.persistence.entities.ressources;

import com.volavis.veraplan.spring.persistence.entities.User;

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

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "timeconstraint_timeslots",
            joinColumns = @JoinColumn(name = "timeconstraint_id"),
            inverseJoinColumns = @JoinColumn(name = "timeslot_id"))
    private List<TimeSlot> timeSlots = new ArrayList<>();


    private int importance = 0;

    @Size(max = 256)
    private String description;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    public TimeConstraint() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
