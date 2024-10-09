package fr.benvolat;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws SQLException {
        Statement statement;
        Connection dbCon;
        try{
            dbCon = DBConnection.getConnection();
            statement = dbCon.createStatement();
            String query = "CREATE TABLE users( id VARCHAR(255) PRIMARY KEY, name VARCHAR(255) NOT NULL, email VARCHAR(255) NOT NULL UNIQUE, role VARCHAR(255) NOT NULL)";
            dbCon.prepareStatement(query).execute();
            System.out.println("Hello world! table user created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }
    }
}