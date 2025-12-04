# Cloud-Based-Inventory-Management-System
This project is a simple and efficient Inventory Management System built using Java RMI with a cloud-hosted MySQL database on AWS RDS. It helps users manage product inventory in real time from any connected client. The system supports basic inventory operations and displays total stock details with a user-friendly GUI.

## Features
- Add, update, delete, and view product records
- Display total items and inventory value
- Search products quickly
- Cloud-based centralized database
- User-friendly Java Swing UI

## Technologies Used
- Java
- Java Swing
- Java RMI
- MySQL (AWS RDS)

### Create SQL Database
```sql
Create SQL Database
CREATE DATABASE inventorydb;

USE inventorydb;

CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    price DOUBLE NOT NULL
);

```
## Database Setup (AWS RDS)
- Create a MySQL instance on AWS RDS and Note down:
- Host endpoint
- Port (default: 3306)
- Database name
- Username & Password
- Enable inbound rule for 3306 in Security Group
- Update DB credentials in DBConnection.java before running

## Update Database Credentials
In DBConnection.java, update:
- Host
- Port
- Database Name
- Username & Password

## Compile Code
javac -cp ".;mysql-connector-j-9.5.0.jar" server\*.java client\*.java common\*.java

## Start RMI Registry
rmiregistry 5000

## Run Server & Client
java server.InventoryServer
java client.InventoryClient
