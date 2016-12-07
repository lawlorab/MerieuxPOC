package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alawlor on 12/7/2016.
 */
@RestController
public class ProductController {
    @RequestMapping(value="/product", method=RequestMethod.GET)
    public String getProducts() {
        try {
            List products = getProductsFromDb();
            ObjectMapper om = new ObjectMapper();
            return om.writeValueAsString(products);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

    @RequestMapping(value="/product", method=RequestMethod.GET, params="id")
    public String getProduct(@RequestParam("id") Long id) {
        try {
            Product product = getProductFromDb(id);
            ObjectMapper om = new ObjectMapper();
            return om.writeValueAsString(product);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

    @RequestMapping(value="/product", method=RequestMethod.POST)
    public String createProduct(@RequestBody Product product) {
        try {
            postProduct(product);
            return "OK";
        }
        catch (SQLException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    @RequestMapping(value="/product", method=RequestMethod.PUT, params="id")
    public String updateProduct(@RequestParam("id") Long id, @RequestBody Product product) {
        try {
            putProduct(id, product);
            return "OK";
        }
        catch (SQLException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    @RequestMapping(value="/product", method=RequestMethod.DELETE, params="id")
    public String deleteProduct(@RequestParam("id") Long id) {
        try {
            removeProduct(id);
            return "OK";
        }
        catch (SQLException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    private static List<Product> getProductsFromDb() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","WestMonroe");
        PreparedStatement stmt = conn.prepareStatement("select * from PRODUCT");
        ResultSet rs = stmt.executeQuery();
        List<Product> productList = new ArrayList<Product>();
        while (rs.next()) {
            Product product = new Product();
            product.setId(rs.getLong(1));
            product.setName(rs.getString(2));
            product.setPrice(rs.getDouble(3));
            productList.add(product);
        }
        return productList;
    }

    private static Product getProductFromDb(Long id) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","WestMonroe");
        PreparedStatement stmt = conn.prepareStatement("select * from PRODUCT where id = ?");
        stmt.setLong(1, id);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        Product product = new Product();
        product.setId(rs.getLong(1));
        product.setName(rs.getString(2));
        product.setPrice(rs.getDouble(3));
        return product;
    }

    private static int postProduct(Product product) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","WestMonroe");
        PreparedStatement stmt = conn.prepareStatement("insert into Product (name, price) values(?,?)");
        stmt.setString(1, product.getName());
        stmt.setDouble(2, product.getPrice());
        return stmt.executeUpdate();
    }

    private static void putProduct(Long id, Product product) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","WestMonroe");
        PreparedStatement stmt = conn.prepareStatement("update Product set name = ?, price = ? where id = ?");
        stmt.setString(1, product.getName());
        stmt.setDouble(2, product.getPrice());
        stmt.setLong(3, id);
        stmt.executeUpdate();
    }

    private static void removeProduct(Long id) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","WestMonroe");
        PreparedStatement stmt = conn.prepareStatement("delete from Product where id = ?");
        stmt.setLong(1, id);
        stmt.executeUpdate();
    }
}
