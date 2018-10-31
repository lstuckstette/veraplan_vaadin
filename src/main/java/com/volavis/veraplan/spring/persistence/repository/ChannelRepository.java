package com.volavis.veraplan.spring.persistence.repository;

import com.volavis.veraplan.spring.persistence.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {

    Optional<Channel> findByName(String name);

    Boolean existsByName(String name);

}
