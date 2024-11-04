package fr.benvolat.models;

public class Admin extends User {

    public Admin(int id, String name, String email, String password) {
        super(id, name, email, password, ROLE.MODERATOR.toString());
    }

    public Admin(String name, String email, String password) {
        super(name, email, password, ROLE.ADMIN.toString());
    }
}
