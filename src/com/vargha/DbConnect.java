package com.vargha;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnect {

    private DbConnect() {
    }

    public static DbConnect getInstance(){
        return new DbConnect();
    }

    public Connection getConnection (){
        try {
            Class.forName("org.sqlite.JDBC");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:adminDb.db");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return connection;

    }
}
