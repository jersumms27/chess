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
        AuthData authData;
        //Check if user already exists
        try {
            userDAO.getUser(request.username());
        } catch (DataAccessException ex) {
            //Create new user
            try {
                userDAO.createUser(new UserData(request.username(), request.password(), request.email()));
            } catch (DataAccessException ex2) {
                return new RegisterResponse(null, null, "Error: already taken"); //[403]
            }
            try {
                authData = authDAO.createAuth(request.username());
            } catch (DataAccessException ex3) {
                return new RegisterResponse(null, null, "Error: bad request"); //[400]
            }

            return new RegisterResponse(request.username(), authData.authToken(), null); //[200]
        }
        return new RegisterResponse(null, null, "Error: already taken"); //[403]
    }

    // parameter: username, password
    // return: username, authToken
    public LoginResponse login(LoginRequest request) {
        LoginResponse error = new LoginResponse(null, null, "Error: unauthorized"); //[401]
        //Check if user already exists
        try {
            userDAO.getUser(request.username());
        } catch (DataAccessException ex) {
            return error;
        }
        //Check if password is correct
        try {
            userDAO.checkPassword(request.username(), request.password());
        } catch (DataAccessException ex2) {
            return error;
        }
        //Create authToken
        AuthData authData;
        try {
            authData = authDAO.createAuth(request.username());
        } catch (DataAccessException ex3) {
            try {
                return new LoginResponse(request.username(), authDAO.getAuth(request.username()), null);
            } catch (DataAccessException ex4) {
                return error;
            }
        }
        return new LoginResponse(request.username(), authData.authToken(), null); //[200]

    }

    // parameter: authToken?
    // return:
    public void logout(LogoutRequest request) {

    }
}
