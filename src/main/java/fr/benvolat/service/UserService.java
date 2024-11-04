package fr.benvolat.service;

import fr.benvolat.dao.UserDataAccess;
import fr.benvolat.models.Admin;
import fr.benvolat.models.Benevole;
import fr.benvolat.models.Moderateur;
import fr.benvolat.models.User;

import java.sql.SQLException;

public class UserService {

    private final UserDataAccess userDAO;

    public UserService() throws SQLException {
        this.userDAO = new UserDataAccess();
    }

    public boolean registerUser(String name, String email, String password, String role) {
        boolean result = false;
        // There is no user registered with that email
        if (userDAO.findUserByEmail(email) == null) {
            switch (role) {
                case "ADMIN":
                    userDAO.addUser(new Admin(name, email, password));
                    break;
                case "USER":
                    userDAO.addUser(new User(name, email, password));
                    break;
                case "BENEVOLE":
                    userDAO.addUser(new Benevole(name, email, password));
                    break;
                case "MODERATOR":
                    userDAO.addUser(new Moderateur(name, email, password));
            }

            result = true;
        }
        return result;
    }

    public User authenticateUser(String email, String password) {
        User user = userDAO.findUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean updateUser(String email) {
        boolean result = false;
        User user = userDAO.findUserByEmail(email);
        if (user != null) {
            result = userDAO.updateUser(user);
        }
        return result;
    }

    public boolean deleteUser(String email) {
        boolean result = false;
        User us = userDAO.findUserByEmail(email);
        if (us != null) {
            result = userDAO.deleteUser(us.getUserID());
        }
        return result;
    }


    public boolean sendRewiew(int userID, String review) {
        return userDAO.sendReview(userID, review);
    }
}
