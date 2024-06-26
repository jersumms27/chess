package service;

import chess.ChessGame;
import dataAccess.*;
import model.GameData;
import service.request.CreateGameRequest;
import service.request.JoinGameRequest;
import response.CreateGameResponse;
import response.JoinGameResponse;
import response.ListGamesResponse;
import webSocketMessages.userCommands.MakeMoveCommand;

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
            return new JoinGameResponse(null, "Error: bad request"); //[400]
        }
        //Check if color is taken
        if (request.playerColor() != null) {
            try {
                gameDAO.verifyColor(Integer.parseInt(request.gameID()), request.playerColor());
            } catch (DataAccessException ex2) {
                return new JoinGameResponse(null,"Error: already taken"); //[403]
            }

            GameData update;
            try {
                if (request.playerColor().equals("WHITE")) {
                    update = new GameData(Integer.parseInt(request.gameID()), authDAO.getUser(authToken), gameData.blackUsername(), gameData.gameName(), gameData.game());
                } else {
                    update = new GameData(Integer.parseInt(request.gameID()), gameData.whiteUsername(), authDAO.getUser(authToken), gameData.gameName(), gameData.game());
                }
            } catch (DataAccessException ex3) {
                return new JoinGameResponse(null, "Error: unauthorized"); //[401]
            }
            try {
                gameDAO.updateGame(update);
            } catch (DataAccessException ex4) {
                return new JoinGameResponse(null,"Error: bad request"); //[400]
            }
        } else {
            try {
                authDAO.getAuth(authToken);
            } catch (DataAccessException ex5) {
                return new JoinGameResponse(null,"Error: unauthorized"); //[401]
            }
        }

        return new JoinGameResponse(gameData.game(), ""); //[200]
    }

    public void updateGame(ChessGame newGame, int gameID, String leavingPlayer) throws DataAccessException {
        GameData previous = gameDAO.getGame(gameID);
        String newWhiteUsername = previous.whiteUsername();
        String newBlackUsername = previous.blackUsername();
        if (leavingPlayer.equals(previous.whiteUsername())) {
            newWhiteUsername = null;
        } else if (leavingPlayer.equals(previous.blackUsername())) {
            newBlackUsername = null;
        }
        GameData update = new GameData(gameID, newWhiteUsername, newBlackUsername, previous.gameName(), newGame);

        gameDAO.updateGame(update);
    }
}
