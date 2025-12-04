package client;

import common.InventoryInterface;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.Naming;
import java.util.List;

public class InventoryClient extends JFrame {
    private InventoryInterface stub;
    private JTextField idField, nameField, qtyField, priceField, searchField;
    private JTable table;
    private DefaultTableModel model;
    private JLabel totalItemsLabel, totalValueLabel;

    public InventoryClient() {
        try {
            // Update HOST and PORT as per your RMI server configuration
            String rmiUrl = "rmi://localhost:5000/inventory";
            System.out.println("Connecting to RMI server at " + rmiUrl + " ...");
            stub = (InventoryInterface) Naming.lookup(rmiUrl);
            System.out.println("Connected successfully to RMI server.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Could not connect to server.\n" + e.getMessage(),
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        setTitle("Cloud-Based Inventory Management");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(230, 240, 255)); // Light azure background

        // Header
        JLabel header = new JLabel("InVora", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.setForeground(new Color(0, 70, 140)); // Dark azure
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(header, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        formPanel.setBackground(new Color(230, 240, 255));

        idField = new JTextField();
        nameField = new JTextField();
        qtyField = new JTextField();
        priceField = new JTextField();

        formPanel.add(labeledField("Product ID:", idField));
        formPanel.add(labeledField("Product Name:", nameField));
        formPanel.add(labeledField("Quantity:", qtyField));
        formPanel.add(labeledField("Price:", priceField));

        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(230, 240, 255));

        JButton addBtn = styledButton("Add Product", new Color(0, 120, 215));
        JButton updateBtn = styledButton("Update Product", new Color(0, 90, 158));
        JButton deleteBtn = styledButton("Delete Product", new Color(200, 50, 50));
        JButton viewBtn = styledButton("View Products", new Color(0, 153, 255));

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(viewBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Table Section
        String[] cols = {"ID", "Name", "Quantity", "Price"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(0, 70, 140));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(204, 229, 255));
        table.setGridColor(new Color(200, 200, 200));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Product Inventory"));
        scroll.setPreferredSize(new Dimension(450, 0));

        // Right-side panel (Table + Search + Stats)
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(new Color(230, 240, 255));

        // Search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        searchPanel.setBackground(new Color(230, 240, 255));
        searchField = new JTextField(20);
        JButton searchBtn = styledButton("Search", new Color(0, 102, 204));

        searchBtn.addActionListener(e -> searchProducts());
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        rightPanel.add(searchPanel, BorderLayout.NORTH);

        // Add table
        rightPanel.add(scroll, BorderLayout.CENTER);

        // Total summary panel
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        summaryPanel.setBackground(new Color(230, 240, 255));
        totalItemsLabel = new JLabel("Total Items: 0");
        totalValueLabel = new JLabel("Total Inventory Value: ₹0.00");
        totalItemsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        summaryPanel.add(totalItemsLabel);
        summaryPanel.add(totalValueLabel);
        rightPanel.add(summaryPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.EAST);

        // Button actions
        addBtn.addActionListener(e -> addProduct());
        updateBtn.addActionListener(e -> updateProduct());
        deleteBtn.addActionListener(e -> deleteProduct());
        viewBtn.addActionListener(e -> viewProducts());
    }

    private JPanel labeledField(String label, JTextField field) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        p.add(l, BorderLayout.NORTH);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 230), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        p.add(field, BorderLayout.CENTER);
        p.setBackground(new Color(230, 240, 255));
        return p;
    }

    private JButton styledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });
        return btn;
    }

    private void addProduct() {
        try {
            if (stub == null) {
                JOptionPane.showMessageDialog(this, "Not connected to server!");
                return;
            }

            String name = nameField.getText().trim();
            String qtyText = qtyField.getText().trim();
            String priceText = priceField.getText().trim();

            if (name.isEmpty() || qtyText.isEmpty() || priceText.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please fill all fields before adding a product.",
                        "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int qty;
            double price;
            try {
                qty = Integer.parseInt(qtyText);
                price = Double.parseDouble(priceText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter valid numeric values for Quantity and Price.",
                        "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String result = stub.addProduct(name, qty, price);
            JOptionPane.showMessageDialog(this, result);
            clearFields();
            viewProducts();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateProduct() {
        try {
            if (stub == null) {
                JOptionPane.showMessageDialog(this, "Not connected to server!");
                return;
            }

            String idText = idField.getText().trim();
            String qtyText = qtyField.getText().trim();

            if (idText.isEmpty() || qtyText.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter both Product ID and Quantity to update.",
                        "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id, qty;
            try {
                id = Integer.parseInt(idText);
                qty = Integer.parseInt(qtyText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter valid numeric values for ID and Quantity.",
                        "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String result = stub.updateProduct(id, qty);
            JOptionPane.showMessageDialog(this, result);
            clearFields();
            viewProducts();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void deleteProduct() {
        try {
            if (stub == null) {
                JOptionPane.showMessageDialog(this, "Not connected to server!");
                return;
            }
            String idText = idField.getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter Product ID to delete.",
                        "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idText);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete product ID " + id + "?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String result = stub.deleteProduct(id);
                JOptionPane.showMessageDialog(this, result);
                clearFields();
                viewProducts();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Product ID must be a number.",
                    "Invalid Input", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void viewProducts() {
        try {
            if (stub == null) {
                JOptionPane.showMessageDialog(this, "Not connected to server!");
                return;
            }
            List<String> products = stub.viewProducts();
            model.setRowCount(0);
            double totalValue = 0;
            int totalItems = 0;

            for (String prod : products) {
                if (prod.startsWith(" ")) continue;
                String[] parts = prod.split("\\|");
                if (parts.length >= 4) {
                    int qty = Integer.parseInt(parts[2].replace("Qty:", "").trim());
                    double price = Double.parseDouble(parts[3].replace("₹", "").trim());
                    model.addRow(new Object[]{
                            parts[0].trim(), parts[1].trim(), qty, price
                    });
                    totalItems += qty;
                    totalValue += qty * price;
                }
            }

            totalItemsLabel.setText("Total Items: " + totalItems);
            totalValueLabel.setText(String.format("Total Inventory Value: ₹%.2f", totalValue));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void searchProducts() {
        String keyword = searchField.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            viewProducts();
            return;
        }

        try {
            if (stub == null) {
                JOptionPane.showMessageDialog(this, "Not connected to server!");
                return;
            }
            List<String> products = stub.viewProducts();
            model.setRowCount(0);

            for (String prod : products) {
                if (prod.startsWith(" ")) continue;
                String[] parts = prod.split("\\|");
                if (parts.length >= 4 && parts[1].toLowerCase().contains(keyword)) {
                    model.addRow(new Object[]{
                            parts[0].trim(), parts[1].trim(),
                            parts[2].replace("Qty:", "").trim(),
                            parts[3].replace("₹", "").trim()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        qtyField.setText("");
        priceField.setText("");
        searchField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InventoryClient().setVisible(true));
    }
}
