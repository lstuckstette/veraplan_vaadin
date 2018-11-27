package com.volavis.veraplan.spring.views.templateModels;

import com.vaadin.flow.templatemodel.TemplateModel;

public interface AppNavigationModel extends TemplateModel {

    void setIsLoggedIn (boolean loggedIn);

    void setUserName(String userName);

}
