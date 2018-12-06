package com.volavis.veraplan.spring.persistence.repository;

import com.volavis.veraplan.spring.persistence.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findByIdBetween(long offset, long limit);
    
    Optional<User> findByUsernameOrEmail(String username, String email);

    @Query("select u from User u left join fetch u.roles")
    List<User> findAllWithRoles();

    List<User> findByIdIn(List<Long> userIds);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}