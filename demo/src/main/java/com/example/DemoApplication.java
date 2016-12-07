package com.example;

import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		Flyway flyway = new Flyway();
		flyway.setDataSource("jdbc:postgresql://localhost:5432/postgres","postgres","WestMonroe");
		flyway.migrate();
		//Customer customer = getCustomer(1L);
		//List customers = getCustomers();
		//customer.setFirstName("Change Name Test");
		//putCustomer(1L, customer);
		Customer newCustomer = new Customer();
		newCustomer.setFirstName("Yet Another");
		newCustomer.setLastName("Test");
		//int res = postCustomer(newCustomer);
		//deleteCustomer(2L);
		//System.out.println(String.format("%d: %s %s, age %d", customer.getId(), customer.getFirstName(), customer.getLastName(), customer.getAge()));
	}
}
