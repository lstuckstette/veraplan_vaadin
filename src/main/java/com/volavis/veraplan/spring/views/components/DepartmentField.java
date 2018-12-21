package com.volavis.veraplan.spring.views.components;

import java.util.ArrayList;

public enum DepartmentField {

    ID("Id"),
    NAME("Name");

    private final String text;

    DepartmentField(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static String[] getAllDepartmentFields() {
        ArrayList<String> efields = new ArrayList<>();
        for (DepartmentField ufield : DepartmentField.values()) {
            efields.add(ufield.toString());
        }
        return efields.toArray(new String[0]);
    }
}
