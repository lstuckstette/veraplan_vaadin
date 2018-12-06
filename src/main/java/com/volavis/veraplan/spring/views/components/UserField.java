package com.volavis.veraplan.spring.views.components;

import com.volavis.veraplan.spring.persistence.entities.RoleName;

import java.util.ArrayList;

public enum UserField {

    ID("Id"),
    FIRSTNAME("Firstname"),
    LASTNAME("Lastname"),
    USERNAME("Username"),
    EMAIL("Email"),
    ROLE("Role");

    private final String text;

    UserField(String fieldName) {
        this.text = fieldName;
    }

    @Override
    public String toString() {
        return text;
    }

    public static String[] getAllUserFields() {
        ArrayList<String> rnames = new ArrayList<>();
        for (RoleName rname : RoleName.values()) {
            rnames.add(rname.toString());
        }
        return rnames.toArray(new String[0]);
    }
}
