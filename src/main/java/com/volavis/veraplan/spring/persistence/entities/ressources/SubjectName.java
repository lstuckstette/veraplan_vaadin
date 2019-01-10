package com.volavis.veraplan.spring.persistence.entities.ressources;

public enum SubjectName {
    MATH("Mathematik"),
    GERMAN("Deutsch"),
    SCIENCE("Sachkunde"),
    SPORTS("Sport"),
    ART("Kunst"),
    MUSIC("Musik");


    private final String text;

    SubjectName(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
