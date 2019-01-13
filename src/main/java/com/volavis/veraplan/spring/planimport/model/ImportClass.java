package com.volavis.veraplan.spring.planimport.model;

public class ImportClass {

    private String id;

    public ImportClass() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ImportClass) {
            return this.id.equals(((ImportClass) other).getId());
        }
        return false;
    }
}
