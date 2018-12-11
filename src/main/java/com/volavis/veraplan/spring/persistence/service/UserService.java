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
import com.volavis.veraplan.spring.views.components.UserFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;

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

    public int countAll() {
        int count = (int) userRepository.count();
//        logger.info("CA: " + count);
        return count;
    }

    public int countAll(UserFilter filter) {
        int count = (int) userRepository.count(getExampleFromFilter(filter));
        logger.info("CF: " + count);
        return count;
    }

    public Stream<User> getAllInRange(int pageIndex, int pageSize) {
//        logger.info("GAIR (i=" + pageIndex + " s=" + pageSize + ")");
        List<User> userList = userRepository.findAll(PageRequest.of(pageIndex, pageSize)).getContent();

        logger.info("returning " + userList.size());
        return userList.stream();

        //        List<User> userList = userRepository.findByIdBetween(offset + 1, offset + limit);
    }

    public Stream<User> getAllInRange(UserFilter userFilter, int pageIndex, int pageSize) {
//        logger.info("GAIR: f= " + userFilter.getFilterText() + " ft=" + userFilter.getType().toString() + " i=" + pageIndex + " s=" + pageSize);

        List<User> findAll = userRepository.findAll(getExampleFromFilter(userFilter), PageRequest.of(pageIndex, pageSize)).getContent();
        logger.info("returning " + findAll.size() + " items.");
        return findAll.stream();


    }

    private Example<User> getExampleFromFilter(UserFilter filter) {
        User user = new User();

        switch (filter.getType()) {
            case USERNAME:
                user.setUsername(filter.getFilterText());
                break;
            case FIRSTNAME:
                user.setFirst_name(filter.getFilterText());
                break;
            case LASTNAME:
                user.setLast_name(filter.getFilterText());
                break;
            case EMAIL:
                user.setEmail(filter.getFilterText());
                break;
//            case ID:
//                try {
//                    user.setId(Long.valueOf(filter.getFilterText()));
//                } catch (Exception e) {
//                    user.setId(-1337l); //show nothing...
//                }
//                break;
//            case ROLE:
//                RoleName roleName = RoleName.fromString(filter.getFilterText());
//
//                if (roleName != null) {
//                    logger.info("rolename found: " + roleName.toString());
//                    user.setRoles(Arrays.asList(new Role(roleName)));
//                } else {
//                    logger.warn("No RoleName found for filterText: " + filter.getFilterText());
//                    user.setId(-1337l); //show nothing...
//                }
//                break;
            default:
                user.setId(-1337l); //show nothing...
        }

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.STARTING);
        return Example.of(user, matcher);

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

    @Transactional
    public void saveChanges(User user) {
        userRepository.save(user);
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
