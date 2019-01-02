package com.volavis.veraplan.spring.views.components;

public enum Weekday {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

    private String text;

    Weekday(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
