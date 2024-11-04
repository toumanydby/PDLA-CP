package fr.benvolat.models;

public class Benevole extends User {

    public Benevole(int id, String name, String email, String password) {
        super(id, name, email, password, ROLE.BENEVOLE.toString());
    }

    public Benevole(String name, String email, String password) {
        super(name, email, password, ROLE.BENEVOLE.toString());
    }

}
