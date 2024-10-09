package fr.benvolat;

import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UserDataAccess {

    private Connection connection;
    private ArrayList<User> usersList;

    public UserDataAccess() throws SQLException {
        this.connection = DBConnection.getConnection();
        this.usersList = new ArrayList<>();
    }

    public void addUser() throws SQLException {
        Statement statement = connection.createStatement();
        String query = "CREATE TABLE users( id VARCHAR(255) PRIMARY KEY, name VARCHAR(255) NOT NULL, email VARCHAR(255) NOT NULL UNIQUE, role VARCHAR(255) NOT NULL)";
        connection.prepareStatement(query).execute();
    }
}
