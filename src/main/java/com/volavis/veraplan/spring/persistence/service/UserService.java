package com.volavis.veraplan.spring.persistence.service;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.volavis.veraplan.spring.persistence.exception.EntityAlreadyExistsException;
import com.volavis.veraplan.spring.persistence.entities.Role;
import com.volavis.veraplan.spring.persistence.entities.RoleName;
import com.volavis.veraplan.spring.persistence.entities.User;
import com.volavis.veraplan.spring.persistence.repository.RoleRepository;
import com.volavis.veraplan.spring.persistence.repository.UserRepository;
import com.volavis.veraplan.spring.views.components.AppNavigation;
import com.volavis.veraplan.spring.views.components.UserField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

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

    public int countAll(){
        int count= (int) userRepository.count();
        logger.info("COUNT: "+count);
        return count;
    }

    public Stream<User> getAllInRange(int offset, int limit){
        logger.info("getAll ("+offset+","+(offset+limit)+")");
//        return userRepository.findAll(PageRequest.of(offset, limit)).getContent().stream();
        List<User> userList = userRepository.findByIdBetween(offset+1, offset+limit);
        logger.info("return "+userList.size());
        return userList.stream();
    }

    public Stream<User> getAllinRange(Optional<String> filter, UserField filterType, int offset, int limit){
        if(!filter.isPresent()){
            return getAllInRange(offset,limit);
        }
        //TODO: build switch(filterType), and call startswith-repoMethod... possibly with range, too!
        return null;
    }


    public User getByUsernameOrEmail(String emailOrUsername) {
        return userRepository.findByUsernameOrEmail(emailOrUsername, emailOrUsername).orElseThrow(() -> {
            throw new UsernameNotFoundException("Unknown user for input '" + emailOrUsername + "' !'");
        });
    }


    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users;
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
            throw new EntityAlreadyExistsException("User with name '" + username + " " + lastName + "' and email '" + email + "' already exists.");
        }

        User user = new User(firstName, lastName, username, email, passwordEncoder.encode(password));

        //Create Role List:
        ArrayList<Role> roles = new ArrayList<>();
        for (RoleName rname : rolenames) {
            roleRepository.findByName(rname).ifPresent(role -> roles.add(role));
        }
        //Create User if Roles not empty:
        if (!roles.isEmpty()) {
            user.setRoles(roles);
            User result = userRepository.save(user);

        }
    }
}
