package com.volavis.veraplan.spring.persistence.repository;

import com.volavis.veraplan.spring.persistence.entities.organisation.Usergroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsergroupRepository extends JpaRepository<Usergroup, Long> {
}
