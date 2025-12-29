package com.bibliotheque.infra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionTest {

    public static void main(String[] args) {

    String url = "jdbc:mysql://127.0.0.1:3306/bib?useSSL=false&serverTimezone=UTC";
    String user = "root";
    String password = "12345678";


        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to MySQL successfully!");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



