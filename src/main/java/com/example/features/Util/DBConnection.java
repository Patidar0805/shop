package com.example.features.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    public class DBConnection {

        private static final String URL = "jdbc:mysql://localhost:3306/shopdb";
        private static final String USERNAME = "root";
        private static final String PASSWORD = "root";

        /*  private static final String URL = "jdbc:mysql://localhost:3306/shopdb";
        private static final String USERNAME = "root";
        private static final String PASSWORD = "Sarvika@0805";
*/
        private static Connection connection;

        private DBConnection() {
        }

        public static Connection getConnection() throws SQLException {
            if (connection == null || connection.isClosed()) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                } catch (ClassNotFoundException e) {
                    throw new SQLException("MySQL JDBC Driver not found.", e);
                }
            }
            return connection;
        }
    }

