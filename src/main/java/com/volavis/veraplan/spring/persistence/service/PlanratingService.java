package com.volavis.veraplan.spring.persistence.service;

import com.volavis.veraplan.spring.persistence.entities.User;
import com.volavis.veraplan.spring.persistence.entities.ressources.Planrating;
import com.volavis.veraplan.spring.persistence.repository.PlanratingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanratingService {

    private PlanratingRepository planratingRepository;


    public PlanratingService(@Autowired PlanratingRepository planratingRepository) {
        this.planratingRepository = planratingRepository;
    }

    public List<Planrating> getAllFromUser(User user) {
        return planratingRepository.findByUserId(user.getId());
    }

    public Optional<Planrating> getSingleRating(User user, int planId) {
        return planratingRepository.findByUserIdAndPlanId(user.getId(), planId);
    }

    public void saveOrUpdate(User user, int planId, int rating) {
        Optional<Planrating> existing = this.getSingleRating(user, planId);
        if (existing.isPresent()) {
            Planrating planrating = existing.get();
            planrating.setRating(rating);
            planratingRepository.save(planrating);
        } else {
            Planrating newRating = new Planrating();
            newRating.setRating(rating);
            newRating.setUser(user);
            newRating.setPlanId(planId);
            planratingRepository.save(newRating);
        }
    }

    public void deleteAll() {
        planratingRepository.deleteAll();
    }

}
