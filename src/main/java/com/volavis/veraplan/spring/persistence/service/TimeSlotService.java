package com.volavis.veraplan.spring.persistence.service;

import com.volavis.veraplan.spring.persistence.entities.ressources.TimeSlot;
import com.volavis.veraplan.spring.persistence.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;

@Service
public class TimeSlotService {

    private TimeSlotRepository timeSlotRepository;

    @Autowired
    public TimeSlotService(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    public TimeSlot findOrCreate(int timeslotEnum, int weekday) {
        TimeSlot example = new TimeSlot();
        example.setEnumerator(timeslotEnum);
        example.setWeekday(weekday);

        List<TimeSlot> timeSlots = timeSlotRepository.findAll(Example.of(example));
        if (timeSlots.isEmpty()) {
            return timeSlotRepository.save(example);
        } else {
            return timeSlots.get(0);
        }
    }
}
