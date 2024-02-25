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
    public RegisterResponse register(RegisterRequest request) {
        //Check if user already exists
        try {
            userDAO.getUser(request.username());
        } catch (DataAccessException ex) {
            //Create new user
            userDAO.createUser(new UserData(request.username(), request.password(), request.email()));
            try {
                return authDAO.createAuth(request.username());
            } catch (DataAccessException ex2) {
            }
        }
        return null;
    }

    // parameter: username, password
    // return: username, authToken
    public LoginResponse login(LoginRequest request) {
        //Check if user already exists
        try {
            userDAO.getUser(request.username());
        } catch (DataAccessException ex) {

        }
    }

    // parameter: authToken?
    // return:
    public void logout(LogoutRequest request) {

    }
}
