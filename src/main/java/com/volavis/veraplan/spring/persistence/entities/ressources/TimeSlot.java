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

    private Integer enumerator;

    private Integer weekday;

    @Temporal(TemporalType.TIME)
    private Date startTime;


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

    public Integer getEnumerator() {
        return enumerator;
    }

    public void setEnumerator(Integer enumerator) {
        this.enumerator = enumerator;
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
