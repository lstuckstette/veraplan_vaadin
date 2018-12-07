package com.volavis.veraplan.spring.views;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.volavis.veraplan.spring.MainLayout;
import com.volavis.veraplan.spring.persistence.entities.User;
import com.volavis.veraplan.spring.views.components.UserField;
import com.volavis.veraplan.spring.persistence.service.UserService;
import com.volavis.veraplan.spring.views.components.UserFilter;
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
        verticalLayout.add(new H3("Browse all Users or filter the List. Clicking on an item will show an editor window."));

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
        filterTextField.setValueChangeMode(ValueChangeMode.EAGER); //direct
        filterLayout.add(filterLabel, filterSelector, filterTextField);
        verticalLayout.add(filterLayout);

        //<--

        Grid<User> grid = new Grid<>();
        grid.setPageSize(25); //this is variable...

        //Lazy-loading filtered data-provider:
        CallbackDataProvider<User, UserFilter> dataProvider = DataProvider.fromFilteringCallbacks(
                query -> {

                    int offset = query.getOffset();
                    int limit = query.getLimit();
                    int pageSize = grid.getPageSize(); //TODO: this is still buggy!
                    int pageIndex = offset / pageSize;

                    if (offset == 0) { //rare case of 'fetch all' e.x. (0,52)
                        pageSize = limit;
                    }

                    logger.info("(" + offset + "," + limit + ") -> (" + pageIndex + "," + pageSize + ")");

                    Optional<UserFilter> filter = query.getFilter();

                    if (filter.isPresent()) {
                        return userService.getAllInRange(filter.get(), pageIndex, pageSize);
                    } else {
                        return userService.getAllInRange(pageIndex, pageSize);
                    }

                },
                query -> {
                    if (query.getFilter().isPresent()) {
                        return userService.countAll(query.getFilter().get());
                    } else {
                        return userService.countAll();
                    }
                }
        );

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
        grid.setItemDetailsRenderer(new ComponentRenderer<>(user -> {
            VerticalLayout layout = new VerticalLayout(); //TODO: implement editor here!
            layout.add(new Span("derpyderp!"));
            return layout;
        }));


        verticalLayout.add(grid);

        this.add(verticalLayout);
    }

}
