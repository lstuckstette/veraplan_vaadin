package com.volavis.veraplan.spring.persistence.service;

import com.volavis.veraplan.spring.persistence.entities.Role;
import com.volavis.veraplan.spring.persistence.entities.RoleName;
import com.volavis.veraplan.spring.persistence.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public void createAllRoles() {

        // Create specific Roles:
        //Role adminRole = new Role(RoleName.ROLE_ADMIN);
        //Role userRole = new Role(RoleName.ROLE_USER);
        //createRole(adminRole, userRole);

        // Create all possible Roles of enum:
        for (RoleName rName : RoleName.values()) {
            createRole(new Role(rName));
        }


    }

    public void createRole(Role... roles) {
        for (Role role : roles) {
            if (!roleRepository.findByName(role.getName()).isPresent()) {
                roleRepository.save(role);
            }
        }

    }

}
