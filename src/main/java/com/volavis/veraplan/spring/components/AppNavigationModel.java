package com.volavis.veraplan.spring.components;

import com.vaadin.flow.templatemodel.TemplateModel;

public interface AppNavigationModel extends TemplateModel {

    void setIsLoggedIn (boolean loggedIn);

    void setUserName(String userName);

}
