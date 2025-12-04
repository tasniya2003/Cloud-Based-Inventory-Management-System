package server;

import java.sql.*;

public class DBConnection {
    public static Connection getConnection() throws Exception {
        
        String url = "jdbc:mysql://<HOST>:<PORT>/<DB_NAME>";
        String user = "<USERNAME>";
        String pass = "<PASSWORD>"; 

        
        Class.forName("com.mysql.cj.jdbc.Driver");

        return DriverManager.getConnection(url, user, pass);
    }
}
