package com.volavis.veraplan.spring.persistence.repository;

import com.volavis.veraplan.spring.persistence.entities.ressources.TimeConstraint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeConstraintRepository extends JpaRepository<TimeConstraint, Long> {
}
