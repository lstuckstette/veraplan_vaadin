package com.volavis.veraplan.spring.views.views_administration;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.volavis.veraplan.spring.MainLayout;
import com.volavis.veraplan.spring.persistence.entities.Role;
import com.volavis.veraplan.spring.persistence.entities.RoleName;
import com.volavis.veraplan.spring.persistence.entities.User;
import com.volavis.veraplan.spring.persistence.exception.RoleNotFoundException;
import com.volavis.veraplan.spring.persistence.service.RoleService;
import com.volavis.veraplan.spring.views.components.ViewHelper;
import com.volavis.veraplan.spring.views.components.EntityFilter;
import com.volavis.veraplan.spring.views.components.UserField;
import com.volavis.veraplan.spring.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PageTitle("Veraplan - Manage Users")
@Route(value = "administration/users", layout = MainLayout.class)
public class ManageUsersView extends Div {

    private static final Logger logger = LoggerFactory.getLogger(ManageUsersView.class);

    private UserService userService;

    private RoleService roleService;



    @Autowired
    public ManageUsersView(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;

        UI.getCurrent().getPage().addStyleSheet("https://use.fontawesome.com/releases/v5.3.1/css/all.css");
        init();
    }

    private void init() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        //Headline
        verticalLayout.add(new H1("Manage Users"));
        verticalLayout.add(new H3("Browse all Users or filter the List. Clicking on an item will show an editor window."));

        //Filter Searchbar
        HorizontalLayout filterLayout = new HorizontalLayout();
        filterLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        Span filterLabel = new Span("Filter by");

        ComboBox<UserField> filterSelector = new ComboBox<>("Type");
        filterSelector.setRequired(true);
        filterSelector.setItemLabelGenerator(UserField::toString);
        filterSelector.setItems(UserField.values());
        filterSelector.setValue(UserField.USERNAME);

        TextField filterTextField = new TextField("Filter string");
        filterTextField.setValueChangeMode(ValueChangeMode.EAGER); //direct
        filterLayout.add(filterLabel, filterSelector, filterTextField);
        verticalLayout.add(filterLayout);

        //<--

        Grid<User> grid = new Grid<>();


//        grid.setHeightByRows(true);
        grid.setHeight("80vh");
        grid.setPageSize(25); //this is variable...

        //Lazy-loading filtered data-provider:
        CallbackDataProvider<User, EntityFilter<UserField>> dataProvider = ViewHelper.getFilterDataProvider(userService);
//
        ConfigurableFilterDataProvider<User, Void, EntityFilter<UserField>> filterWrapper =
                dataProvider.withConfigurableFilter();


        filterTextField.addValueChangeListener(event -> {
            String filterText = event.getValue();
            if (!filterText.trim().isEmpty() && filterText.trim().length() > 1) {
                filterWrapper.setFilter(new EntityFilter<>(filterText, filterSelector.getValue()));
                logger.info("filter set to: " + filterText);
            } else {
                filterWrapper.setFilter(null);
            }

        });
        //<--
        //grid body

        grid.setDataProvider(filterWrapper);
        grid.addColumn(User::getId).setHeader("Id");
        grid.addColumn(User::getFirst_name).setHeader("Firstname");
        grid.addColumn(User::getLast_name).setHeader("Lastname");
        grid.addColumn(User::getUsername).setHeader("Username");
        grid.addColumn(User::getEmail).setHeader("Email");
        grid.addComponentColumn(user -> {

            Div container = new Div();
            user.getRoles().forEach(role -> container.add(new Span(role.getName().toString() + ";")));
            return container;
        }).setHeader("Roles");
        //enable detail view on click:
        grid.setItemDetailsRenderer(new ComponentRenderer<>(this::getUserEditor));


        verticalLayout.add(grid);

        this.add(verticalLayout);
    }

    private Component getUserEditor(User user) {
        //layout + binder
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        FormLayout layout = new FormLayout();
        Binder<User> binder = new Binder<>();

        //inputs:
        TextField firstname = new TextField();
        firstname.setValueChangeMode(ValueChangeMode.EAGER);

        TextField lastname = new TextField();
        lastname.setValueChangeMode(ValueChangeMode.EAGER);

        TextField username = new TextField();
        username.setValueChangeMode(ValueChangeMode.EAGER);

        TextField email = new TextField();
        email.setValueChangeMode(ValueChangeMode.EAGER);

        Checkbox passwordChangeEnabler = new Checkbox("Change Password"); //Toggles password + retypePassword active

        PasswordField password = new PasswordField();
        password.setLabel("New Password");
        password.setValueChangeMode(ValueChangeMode.EAGER);
        password.setEnabled(false);

        PasswordField retypePassword = new PasswordField();
        retypePassword.setLabel("Retype new Password");
        retypePassword.setValueChangeMode(ValueChangeMode.EAGER);
        retypePassword.setEnabled(false);


        ListBox<Checkbox> roles = new ListBox<>();
        roles.setRenderer(new ComponentRenderer<>(cb -> cb)); //somehow this is needed...
        List<Checkbox> roleItems = Arrays.stream(RoleName.values()).map(rname -> new Checkbox(rname.toString())).collect(Collectors.toList());
        roles.setItems(roleItems);

        //controls:
        Label infoLabel = new Label();
        Button save = new Button("Save");
        save.getElement().setAttribute("theme", "contained");
        Button reset = new Button("Reset");

        //add inputs to layout:
        layout.addFormItem(firstname, "Firstname");
        layout.addFormItem(lastname, "Lastname");
        layout.addFormItem(username, "Username");
        layout.addFormItem(email, "Email");

        FormLayout.FormItem pfi = layout.addFormItem(passwordChangeEnabler, "Password");
        pfi.add(new HtmlComponent("br"));
        pfi.add(password);
        pfi.add(new HtmlComponent("br"));
        pfi.add(retypePassword);


        layout.addFormItem(roles, "Roles");
        layout.add(infoLabel);
        layout.add(new HtmlComponent("br"));

        //add controlls to layout:
        HorizontalLayout actions = new HorizontalLayout();
        actions.setAlignItems(FlexComponent.Alignment.CENTER);
        actions.add(save, reset);


        //only update password, if retype is not empty and equals password
        SerializablePredicate<String> retypePasswordNotEmpty = value ->
                retypePassword.getValue().trim().isEmpty();
        SerializablePredicate<String> retypePasswordEqualsPassword = value ->
                retypePassword.getValue().equals(password.getValue());

        //setup binder(s):
        binder.forField(firstname)
                .withValidator(new StringLengthValidator("Firstname can not be empty.", 1, null))
                .bind(User::getFirst_name, User::setFirst_name);
        binder.forField(lastname)
                .withValidator(new StringLengthValidator("Lastname can not be emtpy.", 1, null))
                .bind(User::getLast_name, User::setLast_name);
        binder.forField(username)
                .withValidator(new StringLengthValidator("Username can not be emtpty.", 1, null))
                .bind(User::getUsername, User::setUsername);
        binder.forField(email)
                .withValidator(new StringLengthValidator("Email can not be emtpy.", 1, null))
                .bind(User::getEmail, User::setEmail);
        PasswordEncoder encoder = new BCryptPasswordEncoder();


        //setup password condition based binder:

        passwordChangeEnabler.addValueChangeListener(state -> {
            password.setEnabled(state.getValue());
            retypePassword.setEnabled(state.getValue());
            if (state.getValue()) { //checkbox checked:
                binder.forField(password)
                        .withValidator(new StringLengthValidator("Password can not be emtpy.", 1, null))
                        .withValidator(retypePasswordEqualsPassword, "Passwords do not match.")
                        .bind(User::getPassword, (usr, pass) -> {
                            usr.setPassword(encoder.encode(pass));

                        });
            } else {
                binder.removeBinding(password);
            }
        });

        roleItems.forEach(roleCb -> {
            binder.forField(roleCb)
                    .withValidator(getAtLeastOneRoleSelectedValidator(roleItems))
                    .bind((usr) -> {
                        return usr.getRoles().stream().anyMatch(role -> role.getName().equals(RoleName.fromString(roleCb.getLabel())));
                    }, (usr, value) -> {
                        //Fetch distinct Role from DB:
                        Role currentRole = null;
                        try {
                            currentRole = roleService.getRole(roleCb.getLabel());
                        } catch (RoleNotFoundException e) {
                            //oO
                        }
                        if (currentRole != null) {
                            //add/remove Role depending on value:
                            List<Role> userRoles = usr.getRoles();
                            if (value) {
                                //set role
                                if (!userRoles.contains(currentRole)) {
                                    logger.info("set role");
                                    userRoles.add(currentRole);
                                    usr.setRoles(userRoles);
                                }
                            } else {
                                //remove role
                                if (userRoles.contains(currentRole)) {
                                    logger.info("remove role");
                                    userRoles.remove(currentRole);
                                    usr.setRoles(userRoles);
                                }
                            }
                        }
                    });
        });

        //Listener for action-buttons:

        save.addClickListener(event -> {
            //show warning Dialog:
            Dialog warning;
            warning = ViewHelper.getConfirmationDialog("Do you really want to save entered changes? This can not be reversed!", evt -> {
                if (binder.writeBeanIfValid(user)) {

                    userService.saveChanges(user);

                    infoLabel.setText("Successfully saved changes.");
                } else {
                    String errorText = ViewHelper.getBinderErrorMessage(binder);
                    infoLabel.setText("Error during save: " + errorText);
                }
            });

            warning.open();
        });

        reset.addClickListener(event -> {
            binder.readBean(user);
            infoLabel.setText("");
        });

        //load current user:
        binder.readBean(user);

        //connect layouts
        verticalLayout.add(layout);
        verticalLayout.add(actions);

        return verticalLayout;
    }

    private Validator<? super Boolean> getAtLeastOneRoleSelectedValidator(List<Checkbox> roleList) {
        return (ctx, result) -> {
            if (roleList.stream().anyMatch(Checkbox::getValue)) {
                return ValidationResult.ok();
            } else {
                return ValidationResult.error("At least one Role has to be selected.");
            }
        };
    }

}
