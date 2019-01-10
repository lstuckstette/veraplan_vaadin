package com.volavis.veraplan.spring.persistence.entities.ressources;

public enum RoomType {
    DEFAULT("Normal"),
    GYM("Sporthalle");

    private final String name;

    RoomType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
