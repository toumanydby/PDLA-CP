package fr.benvolat;

import fr.benvolat.dao.MissionDataAccess;
import fr.benvolat.dao.UserDataAccess;
import fr.benvolat.models.Admin;
import fr.benvolat.models.Mission;
import fr.benvolat.models.User;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {

        UserDataAccess userDAO = new UserDataAccess();
        MissionDataAccess missionDAO = new MissionDataAccess();
        User adminUser = new Admin("Aurelien", "anglade@insa-toulouse.fr", "1234567asd");
        userDAO.addUser(adminUser);
        User userRomain = new User("Romain", "romain.dubois@gmail.com", "romain29403");
        userDAO.addUser(userRomain);
        int romainId = userRomain.getUserID();

        Mission m1 = new Mission("Aide pour personnes agees", romainId, "J'ai besoin d'aide pour effectuer un deplacement de certaines de mes affaires depuis mon ancien centre hospitalier au nouveau, je n'ai que trois valises et quelques cartons, merci d'avance pour votre aide");
        missionDAO.addMissionRequest(m1);
    }
}