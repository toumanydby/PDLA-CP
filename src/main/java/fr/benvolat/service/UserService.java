package fr.benvolat.service;

import fr.benvolat.dao.UserDataAccess;
import fr.benvolat.models.User;

import java.sql.SQLException;

public class UserService {

    private final UserDataAccess userDAO;

    public UserService() throws SQLException {
        this.userDAO = new UserDataAccess();
    }

    public boolean registerUser(String name, String email, String password) {
        boolean result = false;
        // There is no user registered with that email
        if( userDAO.findUserByEmail(email) == null){
            userDAO.addUser(new User(name, email, password));
            result = true;
        }
        return result;
    }

    public User authenticateUser(String email, String password){
        User user = userDAO.findUserByEmail(email);
        if(user != null && user.getPassword().equals(password)){
            return user;
        }
        return null;
    }

    public boolean updateUser(String email){
        boolean result = false;
        User user = userDAO.findUserByEmail(email);
        if( user != null){
            result = userDAO.updateUser(user);
        }
        return result;
    }

    public boolean deleteUser(String email){
        boolean result = false;
        User us = userDAO.findUserByEmail(email);
        if( us != null){
            result = userDAO.deleteUser(us.getUserID());
        }
        return result;
    }





}
