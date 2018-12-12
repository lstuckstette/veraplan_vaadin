package com.volavis.veraplan.spring.views;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.flow.component.Component;
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
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.volavis.veraplan.spring.MainLayout;
import com.volavis.veraplan.spring.persistence.entities.Role;
import com.volavis.veraplan.spring.persistence.entities.RoleName;
import com.volavis.veraplan.spring.persistence.entities.User;
import com.volavis.veraplan.spring.persistence.exception.RoleNotFoundException;
import com.volavis.veraplan.spring.persistence.service.RoleService;
import com.volavis.veraplan.spring.views.components.UserField;
import com.volavis.veraplan.spring.persistence.service.UserService;
import com.volavis.veraplan.spring.views.components.UserFilter;
import org.hibernate.annotations.Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@PageTitle("Veraplan - Manage Users")
@Route(value = "administration/users", layout = MainLayout.class)
public class ManageUsersView extends Div {

    private static final Logger logger = LoggerFactory.getLogger(ManageUsersView.class);

    private UserService userService;

    private RoleService roleService;

    //TODO: port this to manageUsersView & delete this

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
        CallbackDataProvider<User, UserFilter> dataProvider = getDataProvider(grid);

        ConfigurableFilterDataProvider<User, Void, UserFilter> filterWrapper =
                dataProvider.withConfigurableFilter();

        filterTextField.addValueChangeListener(event -> {
            String filterText = event.getValue();
            if (!filterText.trim().isEmpty() && filterText.trim().length() > 1) {
                filterWrapper.setFilter(new UserFilter(filterText, filterSelector.getValue()));
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
            user.getRoles().forEach(role -> container.add(new Span(role.getName().toString())));
            return container;
        }).setHeader("Roles");
        //enable detail view on click:
        grid.setItemDetailsRenderer(new ComponentRenderer<>(this::getUserEditor));


        verticalLayout.add(grid);

        this.add(verticalLayout);
    }

    private Component getUserEditor(User user) {
        //layout + binder
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

        PasswordField password = new PasswordField();
        password.setValueChangeMode(ValueChangeMode.EAGER);


        ListBox<Checkbox> roles = new ListBox<>();
        roles.setRenderer(new ComponentRenderer<>(cb -> cb)); //somehow this is needed...
        List<Checkbox> roleItems = Arrays.stream(RoleName.values()).map(rname -> new Checkbox(rname.toString())).collect(Collectors.toList());
        roles.setItems(roleItems);
//        VerticalLayout roles = new VerticalLayout(); // TODO:
//        Arrays.asList(RoleName.values()).forEach(rname -> roles.add(new Checkbox(rname.toString())));


        //controls:
        Label infoLabel = new Label();
        Button save = new Button("Save");
        Button reset = new Button("Reset");

        //add inputs to layout:
        layout.addFormItem(firstname, "Firstname");
        layout.addFormItem(lastname, "Lastname");
        layout.addFormItem(username, "Username");
        layout.addFormItem(email, "Email");
        layout.addFormItem(password, "Password");
        layout.addFormItem(roles, "Roles");
        layout.add(infoLabel);

        //add controlls to layout:
        HorizontalLayout actions = new HorizontalLayout();
        actions.add(save, reset);
        layout.add(actions);

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
        binder.forField(password)
                .withValidator(new StringLengthValidator("Password can not be emtpy.", 1, null))
                .bind(User::getPassword, (usr, pass) -> {
                    usr.setPassword(encoder.encode(pass));
                });

        roleItems.forEach(roleCb -> {
            binder.forField(roleCb)
                    .withValidator(getAtLeastOneRoleSelectedValidator(roleItems))
                    .bind((usr) -> {
                        return usr.getRoles().stream().anyMatch(role -> role.getName().equals(RoleName.fromString(roleCb.getLabel())));
                        //TODO check role exists by roleCB.label
                    }, (usr, value) -> {
                        Role newRole = null;
                        try {
                            newRole = roleService.getRole(roleCb.getLabel());
                        } catch (RoleNotFoundException e) {
                            //oO
                        }
                        if (newRole != null) {
                            List<Role> userRoles = usr.getRoles();
                            userRoles.add(newRole);
                            usr.setRoles(userRoles);
                        }
                        //TODO: add single role by roleCb.label
                    });
        });

        //Listener for action-buttons:

        save.addClickListener(event -> {
            //show warning Dialog:
            Dialog warning = new Dialog();
            warning.setCloseOnEsc(true);
            warning.setCloseOnOutsideClick(true);
            VerticalLayout wLayout = new VerticalLayout();
            wLayout.setAlignItems(FlexComponent.Alignment.CENTER);
            wLayout.add(new Span("Do you really want to save entered changes? This can not be reversed!"));
            Button wConfirm = new Button("Confirm", evt -> {
                if (binder.writeBeanIfValid(user)) {
                    //userService.saveChanges(user);
                    infoLabel.setText("Successfully saved changes.");
                } else {
                    BinderValidationStatus<User> validate = binder.validate();
                    String errorText = validate.getFieldValidationStatuses()
                            .stream().filter(BindingValidationStatus::isError)
                            .map(BindingValidationStatus::getMessage)
                            .map(Optional::get).distinct()
                            .collect(Collectors.joining(", "));
                    infoLabel.setText("Error during save: " + errorText);
                }
                warning.close();
            });
            Button wCancel = new Button("Cancel", evt -> warning.close());
            HorizontalLayout wActions = new HorizontalLayout();
            wActions.add(wConfirm, wCancel);
            wLayout.add(wActions);
            warning.add(wLayout);
            warning.open();
        });

        reset.addClickListener(event -> {
            binder.readBean(user);
            infoLabel.setText("");
        });

        //load current user:
        binder.readBean(user);
        return layout;
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

    private CallbackDataProvider<User, UserFilter> getDataProvider(Grid grid) {
        return DataProvider.fromFilteringCallbacks(
                query -> {

                    int offset = query.getOffset();
                    int limit = query.getLimit();
//                    int pageSize = grid.getPageSize();
//                    int pageIndex = offset / pageSize;
//
//                    if (offset == 0) { //rare case of 'fetch all' e.x. (0,52)
//                        pageSize = limit;
//                    }

//                    logger.info("(" + offset + "," + limit + ") -> (" + pageIndex + "," + pageSize + ")");

                    Optional<UserFilter> filter = query.getFilter();

                    if (filter.isPresent()) {
                        return userService.getAllInRange(filter.get(), offset, limit);
//                        return userService.getAllInRange(filter.get(), pageIndex, pageSize);
                    } else {
                        return userService.getAllInRange(offset, limit);
//                        return userService.getAllInRange(pageIndex, pageSize);
                    }

                },
                query -> {
                    int count;
                    if (query.getFilter().isPresent()) {
                        count = userService.countAll(query.getFilter().get());
                    } else {
                        count = userService.countAll();
                    }
//                    logger.info("Count: " + count);
                    return count;
                }
        );
    }

}
