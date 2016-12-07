package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alawlor on 12/6/2016.
 */
@RestController
public class CustomerController {
    @CrossOrigin
    @RequestMapping(value="/customer", method=RequestMethod.GET)
    public String GetCustomer() {
        try {
            List<Customer> customerList = getCustomers();
            ObjectMapper om = new ObjectMapper();
            return om.writeValueAsString(customerList);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).toString();
        }
    }

    @CrossOrigin
    @RequestMapping(value="/customer", params="id", method=RequestMethod.GET)
    public String GetCustomer(@RequestParam("id") long id) {
        try {
            Customer customer = new Customer();
            customer = getCustomer(id);
            ObjectMapper om = new ObjectMapper();
            return om.writeValueAsString(customer);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).toString();
        }
    }

    @CrossOrigin
    @RequestMapping(value="/customer", params="id", method=RequestMethod.DELETE)
    public String DeleteCustomer(@RequestParam("id") long id) {
        try {
            deleteCustomer(id);
            return "User Deleted";
        }
        catch (SQLException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    @CrossOrigin
    @RequestMapping(value="/customer", params="id", method=RequestMethod.PUT)
    public String UpdateCustomer(@RequestParam("id") long id, @RequestBody Customer customer) {
        try {
            putCustomer(id, customer);
            return "OK";
        }
        catch (SQLException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    @CrossOrigin
    @RequestMapping(value="/customer", method=RequestMethod.POST)
    public String CreateCustomer(@RequestBody Customer customer) {
        try {
            int res = postCustomer(customer);
            return "Result of post was " + res;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    private static List<Customer> getCustomers() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","WestMonroe");
        PreparedStatement stmt = conn.prepareStatement("select * from CUSTOMER");
        ResultSet rs = stmt.executeQuery();
        List<Customer> customerList = new ArrayList<Customer>();
        while (rs.next()) {
            Customer customer = new Customer();
            customer.setId(rs.getLong(1));
            customer.setFirstName(rs.getString(2));
            customer.setLastName(rs.getString(3));
            try {
                customer.setAge(rs.getLong(4));
            }
            catch (NullPointerException e) {
                customer.setAge(null);
            }
            customerList.add(customer);
        }
        return customerList;
    }

    private static Customer getCustomer(Long id) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","WestMonroe");
        PreparedStatement stmt = conn.prepareStatement("select * from CUSTOMER where id = ?");
        stmt.setLong(1, id);
        ResultSet rs = stmt.executeQuery();
        Customer customer = new Customer();
        rs.next();
        customer.setId(rs.getLong(1));
        customer.setFirstName(rs.getString(2));
        customer.setLastName(rs.getString(3));
        try {
            customer.setAge(rs.getLong(4));
        }
        catch (NullPointerException e) {
            customer.setAge(null);
        }
        return customer;
    }

    private static int postCustomer(Customer customer) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","WestMonroe");
        PreparedStatement stmt = conn.prepareStatement("insert into Customer (first_name, last_name, age) values(?,?,?)");
        stmt.setString(1, customer.getFirstName());
        stmt.setString(2, customer.getLastName());
        try {
            stmt.setLong(3, customer.getAge());
        }
        catch (NullPointerException e) {
            stmt.setNull(3, Types.BIGINT);
        }
        return stmt.executeUpdate();
    }

    private static void putCustomer(Long id, Customer customer) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","WestMonroe");
        PreparedStatement stmt = conn.prepareStatement("update Customer set first_name = ?, last_name = ?, age = ? where id = ?");
        stmt.setString(1, customer.getFirstName());
        stmt.setString(2, customer.getLastName());
        stmt.setLong(4, id);
        try {
            stmt.setLong(3, customer.getAge());
        }
        catch (NullPointerException e) {
            stmt.setNull(3, Types.BIGINT);
        }
        stmt.executeUpdate();
    }

    private static void deleteCustomer(Long id) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","WestMonroe");
        PreparedStatement stmt = conn.prepareStatement("delete from Customer where id = ?");
        stmt.setLong(1, id);
        stmt.executeUpdate();
    }
}
