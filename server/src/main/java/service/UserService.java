package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;

public class UserService {
    AuthDAO authDAO;
    UserDAO userDAO;

    public UserService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    // parameter: username, password, email
    // return: username, authToken
    public AuthData register(UserData user) {
        //Check if user already exists
        try {
            userDAO.getUser(user.username());
        } catch (DataAccessException ex) {
            //Create new user
            userDAO.createUser(user);
            try {
                return authDAO.createAuth(user.username());
            } catch (DataAccessException ex2) {
            }
        }
        return null;
    }

    // parameter: username, password
    // return: username, authToken
    public LoginResponse login(LoginRequest request) {

    }

    // parameter: authToken?
    // return:
    public void logout(UserData user) {

    }
}
