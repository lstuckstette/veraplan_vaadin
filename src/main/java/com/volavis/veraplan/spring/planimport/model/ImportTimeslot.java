package com.volavis.veraplan.spring.planimport.model;

public class ImportTimeslot {

    private int day;
    private int slot;

    public ImportTimeslot() {

    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ImportTimeslot) {
            return this.day == ((ImportTimeslot) other).getDay() && this.slot == ((ImportTimeslot) other).getSlot();
        }
        return false;
    }
}
