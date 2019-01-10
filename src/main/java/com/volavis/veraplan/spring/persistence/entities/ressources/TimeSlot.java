package com.volavis.veraplan.spring.persistence.entities.ressources;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "timeslots")
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer timeSlotIndex;

    private Integer weekday;

    @Temporal(TemporalType.TIME)
    private Date startTime;


    @Temporal(TemporalType.TIME)
    private Date endTime;

    public TimeSlot() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTimeSlotIndex() {
        return timeSlotIndex;
    }

    public void setTimeSlotIndex(Integer timeSlotIndex) {
        this.timeSlotIndex = timeSlotIndex;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Integer getWeekday() {
        return weekday;
    }

    public void setWeekday(Integer weekday) {
        this.weekday = weekday;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
