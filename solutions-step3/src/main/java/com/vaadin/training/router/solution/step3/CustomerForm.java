package com.vaadin.training.router.solution.step3;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class CustomerForm extends FormLayout {

    private CustomerService service = CustomerService.getInstance();
    private Customer customer;
    private MyUI myUI;

    private TextField firstName = new TextField("First Name");
    private TextField lastName = new TextField("Last Name");
    private ComboBox<CustomerStatus> status = new ComboBox<>("Status");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");

    private Binder<Customer> binder = new Binder<>(Customer.class);

    public CustomerForm(MyUI myUI) {
        this.myUI = myUI;

        status.setItems(CustomerStatus.values());
        save.addClickShortcut(Key.ENTER);

        // Add form fields and buttons to the layout
        add(firstName, lastName, status, save, delete);

        // Bind fields to the Customer entity
        binder.bindInstanceFields(this);

        // Event handling for save and delete buttons
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        binder.setBean(customer);

        // Show delete button for only customers already in the database
        delete.setVisible(customer.isPersisted());
        setVisible(true);
        //firstName.selectAll();
        firstName.focus();
        firstName.getElement().executeJs("this.inputElement.select();");
    }

    private void delete() {
        if (customer != null) {
            service.delete(customer);
            myUI.updateList();
            setVisible(false);
            Notification.show("Customer deleted", 3000, Notification.Position.MIDDLE);
        }
    }

    private void save() {
        if (customer != null) {
            service.save(customer);
            myUI.updateList();
            setVisible(false);
            Notification.show("Customer saved", 3000, Notification.Position.MIDDLE);
        }
    }
}
