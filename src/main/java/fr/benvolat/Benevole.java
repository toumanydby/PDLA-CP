package fr.benvolat;

import java.util.Date;

public class Benevole extends User {

    public enum STATUS{
        PENDING, VALIDATE, REALISED, REFUSED
    }

    public Benevole(int userID, String name, String email, Date userBirthday) {
        super(userID, name, email, userBirthday);
    }

    public void giveHelp(int missionID) {
        /*
        ID -> missionID
        if (mission_realised.getStatus().equals(Mission.STATUS.VALIDATE.toString())){
            mission_realised.setStatus(Mission.STATUS.REALISED);
        }
        */
    };
}
