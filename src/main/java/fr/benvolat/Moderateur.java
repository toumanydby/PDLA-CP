package fr.benvolat;

import java.util.Date;

public class Moderateur extends User{

    public Moderateur(int userID, String name, String surname, Date userBirthday) {
        super(userID, name, surname, userBirthday);
    }
}
