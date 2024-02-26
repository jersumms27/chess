package handler;

import com.google.gson.JsonObject;
import service.*;

public class GameHandler implements Handler {
    // parameter: authToken
    // return: games
    String listGames(String requestString) {
        ListGamesRequest requestObject = serializer.fromJson(requestString, ListGamesRequest.class);
        ListGamesResponse responseObject = gameService.listGames(requestObject);
        return serializer.toJson(responseObject);
    }

    // parameter: authToken, gameName
    // return: gameID
    String createGame(String requestString) {
        CreateGameRequest requestObject = serializer.fromJson(requestString, CreateGameRequest.class);
        CreateGameResponse responseObject = gameService.createGame(requestObject);
        return serializer.toJson(responseObject);
    }

    // parameter: authToken, playerColor, gameID
    // return:
    String joinGame(String requestString) {
        JoinGameRequest requestObject = serializer.fromJson(requestString, JoinGameRequest.class);
        JoinGameResponse responseObject = gameService.joinGame(requestObject);
        return serializer.toJson(responseObject);
    }
}
