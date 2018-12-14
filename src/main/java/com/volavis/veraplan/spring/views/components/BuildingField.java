package com.volavis.veraplan.spring.views.components;

import java.util.ArrayList;

public enum BuildingField {

    ID("Firstname"),
    NAME("Name");

    private final String text;

    BuildingField(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static String[] getAllUserFields() {
        ArrayList<String> efields = new ArrayList<>();
        for (BuildingField ufield : BuildingField.values()) {
            efields.add(ufield.toString());
        }
        return efields.toArray(new String[0]);
    }


}
