package com.volavis.veraplan.spring.persistence.service;

import com.volavis.veraplan.spring.persistence.entities.ressources.TimeConstraint;
import com.volavis.veraplan.spring.persistence.entities.ressources.TimeSlot;
import com.volavis.veraplan.spring.persistence.repository.TimeConstraintRepository;
import com.volavis.veraplan.spring.persistence.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;

@Service
public class TimeConstraintService {

    private TimeConstraintRepository repository;
    private TimeSlotService timeSlotService;

    @Autowired
    public TimeConstraintService(TimeConstraintRepository repository, TimeSlotService timeSlotService) {
        this.repository = repository;
        this.timeSlotService = timeSlotService;
    }

    public TimeConstraint createTimeConstraint(int timeslotEnum, DayOfWeek weekday, String name, String description) {
        //TODO:
        TimeConstraint timeConstraint = new TimeConstraint();
        TimeSlot timeSlot =  timeSlotService.findOrCreate(timeslotEnum, weekday.getValue());


        return new TimeConstraint();
    }
}
