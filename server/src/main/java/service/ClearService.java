package service;

import dataAccess.*;
import service.response.ClearResponse;

public class ClearService {
    AuthDAO authDAO;
    UserDAO userDAO;
    GameDAO gameDAO;
    public ClearService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    // parameter:
    // return:
    public ClearResponse clear() {
        authDAO.clear();
        userDAO.clear();
        gameDAO.clear();

        return new ClearResponse("");
    }
}
