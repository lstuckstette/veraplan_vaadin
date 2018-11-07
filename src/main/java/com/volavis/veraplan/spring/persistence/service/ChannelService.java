package com.volavis.veraplan.spring.persistence.service;

import com.volavis.veraplan.spring.persistence.model.Channel;
import com.volavis.veraplan.spring.persistence.model.User;
import com.volavis.veraplan.spring.persistence.repository.ChannelAlreadyExistsException;
import com.volavis.veraplan.spring.persistence.repository.ChannelNotFoundException;
import com.volavis.veraplan.spring.persistence.repository.ChannelRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ChannelService {

    @Autowired
    ChannelRepository channelRepository;

    public Channel getByName(String channelName) {
        Channel channel = channelRepository.findByName(channelName).orElseThrow(() -> {
            throw new ChannelNotFoundException("Channel '" + channelName + "' not found.");
        });
        return channel;
    }

    public Channel createChannel(List<User> users) {
        String newUUID = RandomStringUtils.randomAlphanumeric(4, 15); //random channel name
        return createChannel(users, newUUID);


    }

    public Channel createChannel(List<User> users, String name) {
        //check existing:
        if (channelRepository.existsByName(name)) {
            throw new ChannelAlreadyExistsException("Channel already exists!");
        }

        Channel newChannel = new Channel(name);
        newChannel.setUsers(new HashSet<>(users));
        return channelRepository.save(newChannel);
    }

    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    public void deleteChannel(Channel channel) {
        channelRepository.delete(channel);
    }

    public Channel addUsersToChannel(Channel channel, User... users) {
        Set<User> set = channel.getUsers();
        for (User user : users) {
            if (!set.contains(user)) {
                set.add(user);
            }
        }
        channel.setUsers(set);
        return channelRepository.save(channel);
    }

    public Channel removeUsersFromChannel(Channel channel, User... users) {
        Set<User> set = channel.getUsers();
        for (User user : users) {
            if (set.contains(user)) {
                set.remove(user);
            }
        }
        channel.setUsers(set);
        return channelRepository.save(channel);
    }

    @Transactional
    public boolean userInChannel(String channelName, User user) {

        Channel channel = this.getByName(channelName);
        return channel.getUsers().stream().anyMatch(channeluser -> channeluser.getId().equals(user.getId()));

    }
}
