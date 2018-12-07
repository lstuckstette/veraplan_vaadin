package com.volavis.veraplan.spring.views.components;

public class UserFilter {

    private String filterText;
    private UserField type;

    public UserFilter() {
    }

    public UserFilter(String filterText, UserField type) {
        this.filterText = filterText;
        this.type = type;
    }

    public String getFilterText() {
        return filterText;
    }

    public void setFilterText(String filterText) {
        this.filterText = filterText;
    }

    public UserField getType() {
        return type;
    }

    public void setType(UserField type) {
        this.type = type;
    }
}
