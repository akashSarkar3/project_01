package com.vaadin.training.router.solution.step3;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;

/**
 * This class is used as a design template and can be extended with additional logic.
 */
public class CustomerFormDesign extends FormLayout {

    protected TextField firstName;
    protected TextField lastName;
    protected EmailField email;
    protected Select<CustomerStatus> status;
    protected DatePicker birthDate;
    protected Button save;
    protected Button delete;

    public CustomerFormDesign() {
        firstName = new TextField("First Name");
        lastName = new TextField("Last Name");
        email = new EmailField("Email");
        status = new Select<>();
        status.setLabel("Status");
        status.setItems(CustomerStatus.values());
        birthDate = new DatePicker("Birth Date");
        save = new Button("Save");
        delete = new Button("Delete");

        // Add components to the FormLayout
        add(firstName, lastName, email, status, birthDate, save, delete);
    }
}
