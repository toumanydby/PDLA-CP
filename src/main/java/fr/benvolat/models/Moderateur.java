package fr.benvolat.models;

public class Moderateur extends User {


    public Moderateur(int id, String name, String email, String password) {
        super(id, name, email, password, ROLE.MODERATOR.toString());
    }

    public Moderateur(String name, String email, String password) {
        super(name, email, password, ROLE.MODERATOR.toString());
    }
}
