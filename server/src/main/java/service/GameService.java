package service;

import dataAccess.*;
import model.AuthData;
import model.GameData;

import java.util.Collection;

public class GameService {
    AuthDAO authDAO;
    UserDAO userDAO;
    GameDAO gameDAO;
    public GameService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    // parameter:
    // return: list of games
    public ListGamesResponse listGames() {

    }

    // parameter: gameName
    // return: gameID
    public CreateGameResponse createGame(CreateGameRequest request) {

    }

    public void joinGame(JoinGameRequest request) {

    }
}
