package service;

import dataAccess.*;
import model.GameData;
import service.request.CreateGameRequest;
import service.request.JoinGameRequest;
import response.CreateGameResponse;
import response.JoinGameResponse;
import response.ListGamesResponse;

public class GameService {
    AuthDAO authDAO;
    UserDAO userDAO;
    GameDAO gameDAO;
    public GameService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    // parameter: authToken
    // return: list of games
    public ListGamesResponse listGames(String authToken) {
        //Verify authToken
        try {
            authDAO.getAuth(authToken);
            return new ListGamesResponse(gameDAO.listGames(), "");
        } catch (DataAccessException ex) {
            return new ListGamesResponse(null, "Error: unauthorized");
        }
    }

    // parameter: gameName
    // return: gameID
    public CreateGameResponse createGame(String authToken, CreateGameRequest request) {
        //Verify authToken
        try {
            authDAO.getAuth(authToken);
        } catch (DataAccessException ex) {
            return new CreateGameResponse(null, "Error: unauthorized"); //[401]
        }
        try {
            return new CreateGameResponse(String.valueOf((gameDAO.createGame(request.gameName()).gameID())), "");
        } catch (DataAccessException ex2) {
            return new CreateGameResponse(null, "Error: bad request"); //[400]
        }
    }

    // parameter: authToken, playerColor, gameID
    // return:
    public JoinGameResponse joinGame(String authToken, JoinGameRequest request) {
        GameData gameData;
        //Check if game exists
        try {
            gameData = gameDAO.getGame(Integer.parseInt(request.gameID()));
        } catch (DataAccessException ex) {
            return new JoinGameResponse("Error: bad request"); //[400]
        }
        //Check if color is taken
        if (request.playerColor() != null) {
            try {
                gameDAO.verifyColor(Integer.parseInt(request.gameID()), request.playerColor());
            } catch (DataAccessException ex2) {
                return new JoinGameResponse("Error: already taken"); //[403]
            }

            GameData update;
            try {
                if (request.playerColor().equals("WHITE")) {
                    update = new GameData(Integer.parseInt(request.gameID()), authDAO.getUser(authToken), gameData.blackUsername(), gameData.gameName(), gameData.game());
                } else {
                    update = new GameData(Integer.parseInt(request.gameID()), gameData.whiteUsername(), authDAO.getUser(authToken), gameData.gameName(), gameData.game());
                }
            } catch (DataAccessException ex3) {
                return new JoinGameResponse("Error: unauthorized"); //[401]
            }
            try {
                gameDAO.updateGame(update);
            } catch (DataAccessException ex4) {
                return new JoinGameResponse("Error: bad request"); //[400]
            }
        } else {
            try {
                authDAO.getAuth(authToken);
            } catch (DataAccessException ex5) {
                return new JoinGameResponse("Error: unauthorized"); //[401]
            }
        }

        return new JoinGameResponse(""); //[200]
    }
}
