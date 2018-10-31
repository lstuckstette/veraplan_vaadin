package com.volavis.veraplan.spring.persistence.repository;

public class ChannelAlreadyExistsException extends RuntimeException {
    public ChannelAlreadyExistsException(String msg) {
        super(msg);
    }

    public ChannelAlreadyExistsException(String msg, Throwable t) {
        super(msg, t);
    }
}
