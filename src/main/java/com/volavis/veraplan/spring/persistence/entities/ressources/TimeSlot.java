package com.volavis.veraplan.spring.persistence.entities.ressources;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "timeslots")
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date date;

    private int enumerator = -1;

    @NotBlank
    @Temporal(TemporalType.TIME)
    private Date startTime;

    @NotBlank
    @Temporal(TemporalType.TIME)
    private Date endTime;

    public TimeSlot() {
    }

    public TimeSlot(Date startTime, Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TimeSlot(Date startTime, Date endTime, Date date) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getEnumerator() {
        return enumerator;
    }

    public void setEnumerator(int enumerator) {
        this.enumerator = enumerator;
    }

    public Date getStartTime() {
        return startTime;
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
