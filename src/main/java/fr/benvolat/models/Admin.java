package fr.benvolat.models;

import java.sql.SQLException;

public class Admin extends User {

    public Admin(int id,String name, String email,String password) {
        super(id,name, email,password,ROLE.MODERATOR.toString());
    }

    public Admin(String name,String email, String password) {
        super(name,email,password,ROLE.ADMIN.toString());
    }

    public void addBenevole(Benevole benevole) throws SQLException {
       // UserDataAccess.addUser(benevole);
    }
}
