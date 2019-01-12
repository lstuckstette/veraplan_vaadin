package com.volavis.veraplan.spring.planimport.model;

public class ImportRoom {

    private String id;
    private int size;
    private String type;

    public ImportRoom() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
