package service;

import dataAccess.*;
import model.GameData;

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
    public ListGamesResponse listGames(ListGamesRequest request) {
        //Verify authToken
        try {
            authDAO.verifyAuth(request.authToken());
        } catch (DataAccessException ex) {
            return new ListGamesResponse(null, "Error: unauthorized");
        }

        return new ListGamesResponse(gameDAO.listGames(), null);
    }

    // parameter: gameName
    // return: gameID
    public CreateGameResponse createGame(CreateGameRequest request) {
        //Verify authToken
        try {
            authDAO.verifyAuth(request.authToken());
        } catch (DataAccessException ex) {
            return new CreateGameResponse(null, "Error: unauthorized"); //[401]
        }

        return new CreateGameResponse(String.valueOf((gameDAO.createGame(request.gameName()).gameID())), null);
    }

    // parameter: authToken, playerColor, gameID
    // return:
    public JoinGameResponse joinGame(JoinGameRequest request) {
        GameData gameData;
        //Verify authToken
        try {
            authDAO.verifyAuth(request.authToken());
        } catch (DataAccessException ex) {
            return new JoinGameResponse("Error: unauthorized"); //[401]
        }
        //Check if game exists
        try {
            gameData = gameDAO.getGame(Integer.parseInt(request.gameID()));
        } catch (DataAccessException ex2) {
            return new JoinGameResponse("Error: bad request"); //[400]
        }
        //Check if color is taken
        if (request.playerColor() != null) {
            try {
                gameDAO.verifyColor(Integer.parseInt(request.gameID()), request.playerColor());
            } catch (DataAccessException ex3) {
                return new JoinGameResponse("Error: already taken"); //[403]
            }
            gameDAO.updateGame(); //TODO put player in game, only change one color
        }

        return new JoinGameResponse(null); //[200]
    }
}
