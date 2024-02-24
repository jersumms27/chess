package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;

public class UserService {
    AuthDAO authDAO;
    UserDAO userDAO;

    public UserService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }
    public AuthData register(UserData user) {
        //Check if user already exists
        try {
            userDAO.getUser(user.username());
        } catch (DataAccessException ex) {
            //Create new user
            userDAO.createUser(user);
            return authDAO.createAuth(user.username());
        }

        return null;
    }

    public AuthData login(UserData user) {

    }

    public void logout(UserData user) {

    }
}
