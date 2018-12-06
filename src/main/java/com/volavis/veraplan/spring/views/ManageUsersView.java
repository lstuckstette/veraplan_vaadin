package com.volavis.veraplan.spring.views;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.volavis.veraplan.spring.MainLayout;
import com.volavis.veraplan.spring.persistence.entities.User;
import com.volavis.veraplan.spring.views.components.UserField;
import com.volavis.veraplan.spring.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@PageTitle("Veraplan - Manage Users")
@Route(value = "administration/users", layout = MainLayout.class)
public class ManageUsersView extends Div {

    private static final Logger logger = LoggerFactory.getLogger(ManageUsersView.class);

    private UserService userService;

    //TODO: port this to manageUsersView & delete this

    @Autowired
    public ManageUsersView(UserService userService) {
        this.userService = userService;

        UI.getCurrent().getPage().addStyleSheet("https://use.fontawesome.com/releases/v5.3.1/css/all.css");
        init();
    }

    private void init() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        //Headline
        verticalLayout.add(new H1("Manage Users"));

        //Filter
        HorizontalLayout filterLayout = new HorizontalLayout();
        filterLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        Span filterLabel = new Span("Filter by");

        ComboBox<UserField> filterSelector = new ComboBox<>("Type");
        filterSelector.setRequired(true);
        filterSelector.setItemLabelGenerator(UserField::toString);
        filterSelector.setItems(UserField.values());
        filterSelector.setValue(UserField.USERNAME);

        TextField filterTextField = new TextField("Filter string");

        filterLayout.add(filterLabel,filterSelector, filterTextField);
        verticalLayout.add(filterLayout);

        Grid<User> grid = new Grid<>();
        grid.setPageSize(25); //this is variable...


        CallbackDataProvider<User, String> dataProvider = DataProvider.fromFilteringCallbacks(
                query -> {

                    int offset = query.getOffset();
//                    int limit = 20;
                    int limit = query.getLimit();
                    Optional<String> filter = query.getFilter();
                    UserField filterType = filterSelector.getValue();

                    return userService.getAllInRange(offset, limit);
                },
                query -> userService.countAll()
        );
        //Filtering
        ConfigurableFilterDataProvider<User, Void, String> filterWrapper =
                dataProvider.withConfigurableFilter();

        filterTextField.addValueChangeListener(event -> {
            String filterText = event.getValue();
            if (filterText.trim().isEmpty() && filterText.trim().length() > 2) {
                filterText = null;
            }
            filterWrapper.setFilter(filterText);
        });
        //grid body
        grid.setDataProvider(dataProvider);
        grid.addColumn(User::getId).setHeader("Id");
        grid.addColumn(User::getFirst_name).setHeader("Firstname");
        grid.addColumn(User::getLast_name).setHeader("Lastname");
        grid.addColumn(User::getUsername).setHeader("Username");
        grid.addColumn(User::getEmail).setHeader("Email");
        grid.addComponentColumn(user -> {
            ComboBox<String> roleBox = new ComboBox<>();
            roleBox.setItems(user.getRoles().stream().map(role -> role.getName().toString()));
            return roleBox;
        }).setHeader("Roles");


        verticalLayout.add(grid);

        this.add(verticalLayout);
    }

}
