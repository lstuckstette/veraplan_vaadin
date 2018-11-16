package com.volavis.veraplan.spring.persistence.repository;


import com.volavis.veraplan.spring.persistence.entities.Role;
import com.volavis.veraplan.spring.persistence.entities.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}