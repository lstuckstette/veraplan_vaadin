package com.volavis.veraplan.spring.persistence.service;

import com.volavis.veraplan.spring.persistence.entities.User;
import com.volavis.veraplan.spring.persistence.entities.ressources.TimeConstraint;
import com.volavis.veraplan.spring.persistence.entities.ressources.TimeSlot;
import com.volavis.veraplan.spring.persistence.repository.TimeConstraintRepository;
import com.volavis.veraplan.spring.persistence.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Service
public class TimeConstraintService {

    private TimeConstraintRepository repository;
    private TimeSlotService timeSlotService;

    @Autowired
    public TimeConstraintService(TimeConstraintRepository repository, TimeSlotService timeSlotService) {
        this.repository = repository;
        this.timeSlotService = timeSlotService;
    }

    public TimeConstraint createTimeConstraint(int timeslotEnum, DayOfWeek weekday, String name, String description, User user) {

        TimeConstraint timeConstraint = new TimeConstraint();
        TimeSlot timeSlot = timeSlotService.findOrCreate(timeslotEnum, weekday.getValue());
        List<TimeSlot> timeSlotList = new ArrayList<>();
        timeSlotList.add(timeSlot);
        timeConstraint.setTimeSlots(timeSlotList);
        timeConstraint.setName(name);
        timeConstraint.setDescription(description);
        timeConstraint.setUser(user);

        return repository.save(timeConstraint);
    }

    public TimeConstraint saveChanges(TimeConstraint timeConstraint, int timeslotEnum, DayOfWeek weekday, String name, String description) {

        TimeSlot timeSlot = timeSlotService.findOrCreate(timeslotEnum, weekday.getValue());
        List<TimeSlot> timeSlotList = new ArrayList<>();
        timeSlotList.add(timeSlot);
        timeConstraint.setTimeSlots(timeSlotList);
        timeConstraint.setName(name);
        timeConstraint.setDescription(description);
        return repository.save(timeConstraint);
    }

    public List<TimeConstraint> getAllFromUser(User user) {
        return repository.findByUserId(user.getId());

    }

    public List<TimeSlot> getTimeSlots(TimeConstraint timeConstraint) {
        TimeConstraint withTimeSlots = repository.findOneWithTimeSlotsById(timeConstraint.getId());
        return withTimeSlots.getTimeSlots();
    }

    public void removeTimeConstraint(TimeConstraint timeConstraint) {
        repository.delete(timeConstraint);
    }
}
