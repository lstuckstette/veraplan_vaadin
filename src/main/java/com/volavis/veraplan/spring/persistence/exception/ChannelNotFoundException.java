package com.volavis.veraplan.spring.persistence.exception;

public class ChannelNotFoundException extends RuntimeException {

    public ChannelNotFoundException(String msg) {
        super(msg);
    }

    public ChannelNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}
