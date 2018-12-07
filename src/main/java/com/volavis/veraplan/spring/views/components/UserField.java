package com.volavis.veraplan.spring.views.components;

import com.volavis.veraplan.spring.persistence.entities.RoleName;

import java.util.ArrayList;

public enum UserField {

    FIRSTNAME("Firstname"),
    LASTNAME("Lastname"),
    USERNAME("Username"),
    EMAIL("Email");

    private final String text;

    UserField(String fieldName) {
        this.text = fieldName;
    }

    @Override
    public String toString() {
        return text;
    }

    public static String[] getAllUserFields() {
        ArrayList<String> ufields = new ArrayList<>();
        for (UserField ufield : UserField.values()) {
            ufields.add(ufield.toString());
        }
        return ufields.toArray(new String[0]);
    }
}
