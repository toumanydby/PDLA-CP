package fr.benvolat;

import java.util.Date;

public class User {
    private String name;
    private String surname;

    /**
     * ID unique de l'user
     */
    private int userID;

    private Date userBirthday;

    public User(int userID, String name, String surname, Date userBirthday){
        this.userID = userID;
        this.name = name;
        this.surname = surname;
        this.userBirthday = userBirthday;
    }

    public int getUserID(){
        return this.userID;
    }

    public String getUserFullName(){
        return this.surname + this.name;
    }
}
