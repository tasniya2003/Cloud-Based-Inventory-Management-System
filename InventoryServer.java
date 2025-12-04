package server;

import common.InventoryInterface;
import java.rmi.*;
import java.rmi.server.*;
import java.sql.*;
import java.util.*;

public class InventoryServer extends UnicastRemoteObject implements InventoryInterface {

    protected InventoryServer() throws RemoteException {
        super();
    }

    @Override
    public String addProduct(String name, int quantity, double price) throws RemoteException {
        try (Connection con = DBConnection.getConnection()) {
            String q = "INSERT INTO products(name, quantity, price) VALUES(?,?,?)";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setString(1, name);
            ps.setInt(2, quantity);
            ps.setDouble(3, price);
            ps.executeUpdate();
            return "Product added successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @Override
    public String updateProduct(int id, int quantity) throws RemoteException {
        try (Connection con = DBConnection.getConnection()) {
            String q = "UPDATE products SET quantity=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setInt(1, quantity);
            ps.setInt(2, id);
            int rows = ps.executeUpdate();
            return rows > 0 ? "Product updated successfully!" : "Product not found!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @Override
    public String deleteProduct(int id) throws RemoteException {
        try (Connection con = DBConnection.getConnection()) {
            String q = "DELETE FROM products WHERE id=?";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0 ? "Product deleted successfully!" : "Product not found!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @Override
    public List<String> viewProducts() throws RemoteException {
        List<String> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM products");
            while (rs.next()) {
                list.add(rs.getInt("id") + " | " + rs.getString("name") +
                         " | Qty: " + rs.getInt("quantity") +
                         " | ₹" + rs.getDouble("price"));
            }
        } catch (Exception e) {
            list.add("Error: " + e.getMessage());
        }
        return list;
    }

    public static void main(String[] args) {
        try {
            InventoryServer server = new InventoryServer();
            Naming.rebind("rmi://localhost:5000/inventory", server);
            System.out.println("Inventory Server is running on port 5000...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
