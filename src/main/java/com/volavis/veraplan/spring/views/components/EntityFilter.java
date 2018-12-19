package com.volavis.veraplan.spring.views.components;

public class EntityFilter<T extends Enum<T>> {

    private String filterText;
    private T type;


    public EntityFilter(String filterText, T type) {
        this.filterText = filterText;
        this.type = type;
    }

    public String getFilterText() {
        return filterText;
    }

    public void setFilterText(String filterText) {
        this.filterText = filterText;
    }

    public T getType() {
        return type;
    }

    public void setType(T type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "FILTER: type=" + type.name() + " text=" + filterText;
    }

}
