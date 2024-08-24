package com.vaadin.training.router.solution.step3;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.util.List;


@Route("")


public class MyUI extends VerticalLayout {

    private final CustomerService service = CustomerService.getInstance() ;
    private final Grid<Customer> grid = new Grid<>(Customer.class);
    private final TextField filterText = new TextField();
    private final CustomerForm form = new CustomerForm(this);

    public MyUI() {
        // Set up the layout
        filterText.setPlaceholder("Filter by name...");
        filterText.addValueChangeListener(e -> updateList());

        Button clearFilterTextBtn = new Button("Clear");
        clearFilterTextBtn.addClickListener(e -> {
            filterText.clear();
            updateList();
        });

        HorizontalLayout filtering = new HorizontalLayout(filterText, clearFilterTextBtn);
        filtering.setSpacing(true);

        Button addCustomerBtn = new Button("Add new customer");
        addCustomerBtn.addClickListener(e -> {
            grid.asSingleSelect().clear();
            form.setCustomer(new Customer());
        });

        HorizontalLayout toolbar = new HorizontalLayout(filtering, addCustomerBtn);
        toolbar.setSpacing(true);

        grid.setColumns("firstName", "lastName", "email");

        HorizontalLayout main = new HorizontalLayout(grid, form);
        main.setSpacing(true);
        main.setSizeFull();
        grid.setSizeFull();
        main.setFlexGrow(1, grid);

        add(toolbar, main);

        updateList();

        form.setVisible(false);

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() == null) {
                form.setVisible(false);
            } else {
                form.setCustomer(event.getValue());
            }
        });
    }

    public void updateList() {
        List<Customer> customers = service.findAll(filterText.getValue());
        grid.setItems(customers);
    }
}
