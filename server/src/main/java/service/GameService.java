package service;

import dataAccess.*;

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
        return null;
    }

    // parameter: gameName
    // return: gameID
    public CreateGameResponse createGame(CreateGameRequest request) {
        return null;
    }

    public void joinGame(JoinGameRequest request) {

    }
}
