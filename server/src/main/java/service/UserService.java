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
        String authToken;
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
                authToken = authDAO.createAuth(request.username());
            } catch (DataAccessException ex3) {
                return new RegisterResponse(null, null, "Error: bad request"); //[400]
            }

            return new RegisterResponse(request.username(), authToken, ""); //[200]
        }
        return new RegisterResponse(null, null, "Error: already taken"); //[403]
    }

    // parameter: username, password
    // return: username, authToken
    public LoginResponse login(LoginRequest request) {
        LoginResponse error = new LoginResponse(null, null, "Error: unauthorized"); //[401]
        try {
            userDAO.checkPassword(request.username(), request.password());
            String authToken = authDAO.createAuth(request.username());
            return new LoginResponse(request.username(), authToken, ""); //[200]
        } catch (DataAccessException ex) {
            return error;
        }
    }

    // parameter: authToken?
    // return:
    public LogoutResponse logout(String authToken) {
        try {
            authDAO.deleteAuth(authToken);
        } catch (DataAccessException ex) {
            new LogoutResponse("Error: unauthorized"); //[401]
        }
        return new LogoutResponse(""); //[200]
    }
}
