package handler;

import service.request.CreateGameRequest;
import service.request.JoinGameRequest;
import response.CreateGameResponse;
import response.JoinGameResponse;
import response.ListGamesResponse;

public class GameHandler implements Handler {
    // parameter: authToken
    // return: games
    public String listGames(String authToken) {
        ListGamesResponse responseObject = gameService.listGames(authToken);
        return serializer.toJson(responseObject);
    }

    // parameter: authToken, gameName
    // return: gameID
    public String createGame(String authToken, String requestString) {
        CreateGameRequest requestObject = serializer.fromJson(requestString, CreateGameRequest.class);
        CreateGameResponse responseObject = gameService.createGame(authToken, requestObject);
        return serializer.toJson(responseObject);
    }

    // parameter: authToken, playerColor, gameID
    // return:
    public String joinGame(String authToken, String requestString) {
        JoinGameRequest requestObject = serializer.fromJson(requestString, JoinGameRequest.class);
        JoinGameResponse responseObject = gameService.joinGame(authToken, requestObject);
        return serializer.toJson(responseObject);
    }
}
