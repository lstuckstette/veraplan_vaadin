package com.volavis.veraplan.spring.persistence.exception;

public class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException(String msg) {
        super(msg);
    }

    public EntityAlreadyExistsException(String msg, Throwable t) {
        super(msg, t);
    }
}
