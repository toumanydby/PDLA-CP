package fr.benvolat;

import java.util.Date;

public class User {
    /**
     * ID unique de l'user
     */
    private int userID;
    private String name;
    private String email;
    private Date userBirthday;

    public User(int userID, String name, String email, Date userBirthday){
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.userBirthday = userBirthday;
    }

    public int getUserID(){
        return this.userID;
    }

    public String getUserName(){
        return this.name;
    }
}
