package com.volavis.veraplan.spring.persistence.entities;

import java.util.ArrayList;

public enum RoleName {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String text;

    RoleName(String role_user) {
        this.text = role_user;
    }

    @Override
    public String toString() {
        return text;
    }

    public static String[] getAllRoleNames() {
        ArrayList<String> rnames = new ArrayList<>();
        for (RoleName rname : RoleName.values()) {
            rnames.add(rname.toString());
        }
        return rnames.toArray(new String[0]);
    }
}
