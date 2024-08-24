package com.vaadin.training.router.solution.step3;

import com.vaadin.flow.router.Route;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 * A simple in-memory "database" for demo purposes.
 * In a production environment, this would be replaced by a real service class.
 */
public class CustomerService {

	private static CustomerService instance;
	private static final Logger LOGGER = Logger.getLogger(CustomerService.class.getName());

	private final Map<Long, Customer> contacts = new ConcurrentHashMap<>();
	private long nextId = 0;

	private CustomerService() {
		// Initialize with some test data
		ensureTestData();
	}

	/**
	 * @return a singleton instance of CustomerService
	 */
	public static CustomerService getInstance() {
		if (instance == null) {
			instance = new CustomerService();
		}
		return instance;
	}

	/**
	 * @return all available Customer objects.
	 */
	public List<Customer> findAll() {
		return findAll(null);
	}

	/**
	 * Finds all Customers that match the given filter.
	 *
	 * @param stringFilter filter that returned objects should match, or null/empty string if all objects should be returned.
	 * @return list of Customer objects
	 */
	public List<Customer> findAll(String stringFilter) {
		return contacts.values().stream()
				.filter(contact -> {
					if (stringFilter == null || stringFilter.isEmpty()) {
						return true;
					}
					return contact.toString().toLowerCase().contains(stringFilter.toLowerCase());
				})
				.map(this::cloneCustomer)
				.sorted(Comparator.comparingLong(Customer::getId).reversed())
				.collect(Collectors.toList());
	}

	/**
	 * Finds all Customers that match the given filter and limits the result set.
	 *
	 * @param stringFilter filter that returned objects should match, or null/empty string if all objects should be returned.
	 * @param start index of the first result
	 * @param maxResults maximum result count
	 * @return list of Customer objects
	 */
	public List<Customer> findAll(String stringFilter, int start, int maxResults) {
		List<Customer> filteredList = findAll(stringFilter);
		int end = Math.min(start + maxResults, filteredList.size());
		return filteredList.subList(start, end);
	}

	/**
	 * @return the number of all customers in the system
	 */
	public long count() {
		return contacts.size();
	}

	/**
	 * Deletes a customer from the system.
	 *
	 * @param value the Customer to be deleted
	 */
	public void delete(Customer value) {
		contacts.remove(value.getId());
	}

	/**
	 * Persists or updates a customer in the system. Also assigns an identifier for new Customer instances.
	 *
	 * @param entry the Customer to be saved
	 */
	public void save(Customer entry) {
		if (entry == null) {
			LOGGER.log(Level.SEVERE, "Customer is null. Ensure your form is properly connected.");
			return;
		}
		if (entry.getId() == null) {
			entry.setId(nextId++);
		}
		contacts.put(entry.getId(), cloneCustomer(entry));
	}

	/**
	 * Sample data generation
	 */
	private void ensureTestData() {
		if (findAll().isEmpty()) {
			final String[] names = {
					"Gabrielle Patel", "Brian Robinson", "Eduardo Haugen", "Koen Johansen", "Alejandro Macdonald",
					"Angel Karlsson", "Yahir Gustavsson", "Haiden Svensson", "Emily Stewart", "Corinne Davis",
					"Ryann Davis", "Yurem Jackson", "Kelly Gustavsson", "Eileen Walker", "Katelyn Martin",
					"Israel Carlsson", "Quinn Hansson", "Makena Smith", "Danielle Watson", "Leland Harris",
					"Gunner Karlsen", "Jamar Olsson", "Lara Martin", "Ann Andersson", "Remington Andersson",
					"Rene Carlsson", "Elvis Olsen", "Solomon Olsen", "Jaydan Jackson", "Bernard Nilsen"
			};
			Random random = new Random(0);
			for (String name : names) {
				String[] split = name.split(" ");
				Customer customer = new Customer();
				customer.setFirstName(split[0]);
				customer.setLastName(split[1]);
				customer.setEmail(split[0].toLowerCase() + "@" + split[1].toLowerCase() + ".com");
				customer.setStatus(CustomerStatus.values()[random.nextInt(CustomerStatus.values().length)]);
				Calendar cal = Calendar.getInstance();
				int daysOld = -random.nextInt(365 * 15 + 365 * 60);
				cal.add(Calendar.DAY_OF_MONTH, daysOld);
				customer.setBirthDate(cal.getTime());
				save(customer);
			}
		}
	}

	private Customer cloneCustomer(Customer customer) {
		try {
			return (Customer) customer.clone();
		} catch (CloneNotSupportedException ex) {
			LOGGER.log(Level.SEVERE, "Failed to clone Customer", ex);
			return customer; // fallback to original if clone fails
		}
	}
}
