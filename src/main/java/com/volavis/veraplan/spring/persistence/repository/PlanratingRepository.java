package com.volavis.veraplan.spring.persistence.repository;

import com.volavis.veraplan.spring.persistence.entities.ressources.Planrating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanratingRepository extends JpaRepository<Planrating, Long> {

    List<Planrating> findByUserId(long id);

    Optional<Planrating> findByUserIdAndPlanId(long userId, int planId);
}
