package com.volavis.veraplan.spring.persistence.service;

import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import com.volavis.veraplan.spring.persistence.model.User;
import com.volavis.veraplan.spring.persistence.repository.RoleRepository;
import com.volavis.veraplan.spring.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    public UserService() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    public String getFullName(String emailOrUsername) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(emailOrUsername, emailOrUsername).orElseThrow(() -> {
            throw new UsernameNotFoundException("Unknown user for input '" + emailOrUsername + "' !'");
        });
        return user.getFirst_name() + " " + user.getLast_name();
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
}
