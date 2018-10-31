package com.volavis.veraplan.spring.persistence.service;

import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import com.volavis.veraplan.spring.persistence.model.Role;
import com.volavis.veraplan.spring.persistence.model.RoleName;
import com.volavis.veraplan.spring.persistence.model.User;
import com.volavis.veraplan.spring.persistence.repository.RoleRepository;
import com.volavis.veraplan.spring.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
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

    public String getFullName(String emailOrUsername) {
        User user = userRepository.findByUsernameOrEmail(emailOrUsername, emailOrUsername).orElseThrow(() -> {
            throw new UsernameNotFoundException("Unknown user for input '" + emailOrUsername + "' !'");
        });
        return user.getFirst_name() + " " + user.getLast_name();
    }

    public User getByUsernameOrEmail(String emailOrUsername) {
        return userRepository.findByUsernameOrEmail(emailOrUsername, emailOrUsername).orElseThrow(() -> {
            throw new UsernameNotFoundException("Unknown user for input '" + emailOrUsername + "' !'");
        });
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void createUser(User user, RoleName... rolenames) {
        createUser(
                user.getFirst_name(),
                user.getLast_name(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                rolenames
        );
    }

    public void createUser(String firstName, String lastName, String username, String email, String password, RoleName... rolenames) {

        //check existing username/email:
        if (userRepository.findByUsernameOrEmail(username, email).isPresent()) {
            return;
        }

        User user = new User(firstName, lastName, username, email, passwordEncoder.encode(password));

        //Create Role Set:
        ArrayList<Role> roles = new ArrayList<>();
        for (RoleName rname : rolenames) {
            roleRepository.findByName(rname).ifPresent(role -> roles.add(role));
        }
        //Create User if Roles not empty:
        if (!roles.isEmpty()) {
            user.setRoles(new HashSet<>(roles));
            User result = userRepository.save(user);

        }
    }
}
