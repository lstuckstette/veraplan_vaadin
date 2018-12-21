package com.volavis.veraplan.spring.persistence.repository;

import com.volavis.veraplan.spring.persistence.entities.organisation.Building;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface BuildingRepository extends JpaRepository<Building, Long> {

    @EntityGraph(attributePaths = "departments")
    Building findOneWithDepartmentsById(Long id);
}
